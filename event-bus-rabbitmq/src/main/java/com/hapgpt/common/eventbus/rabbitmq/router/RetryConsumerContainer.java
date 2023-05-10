package com.hapgpt.common.eventbus.rabbitmq.router;

import com.hapgpt.common.eventbus.core.exception.EventListenerNotFoundException;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactoryUtils;
import org.springframework.amqp.rabbit.connection.RabbitResourceHolder;
import org.springframework.amqp.rabbit.connection.RabbitUtils;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;
import org.springframework.amqp.rabbit.support.DefaultMessagePropertiesConverter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hapgpt.common.eventbus.rabbitmq.utils.MQUtils.getStackTraceAsString;

@Slf4j
public class RetryConsumerContainer extends SimpleMessageListenerContainer {

    private DefaultMessagePropertiesConverter converter = new DefaultMessagePropertiesConverter();
    private String exchangeName;
    private String queueName;
    private String routingKey;
    private int retryTimes;

    public RetryConsumerContainer(ConnectionFactory connectionFactory) {
        this(connectionFactory, "", "", "", 10);
    }

    public RetryConsumerContainer(ConnectionFactory connectionFactory, String exchangeName, String queueName, String routingKey, int retryTimes) {
        super(connectionFactory);
        this.exchangeName = exchangeName;
        this.queueName = queueName;
        this.routingKey = routingKey;
        this.retryTimes = retryTimes;
    }

    @Override
    protected void doInvokeListener(ChannelAwareMessageListener listener, Channel channel, Message data) {
        Message message = (Message) data; // 不存在list 类型数据
        RabbitResourceHolder resourceHolder = null;
        Channel channelToUse = channel;
        boolean boundHere = false;
        try {
            if (!isExposeListenerChannel()) {
                // We need to expose a separate Channel.
                resourceHolder = getTransactionalResourceHolder();
                channelToUse = resourceHolder.getChannel();
                /*
                 * If there is a real transaction, the resource will have been bound; otherwise
				 * we need to bind it temporarily here. Any work done on this channel
				 * will be committed in the finally block.
				 */
                if (isChannelLocallyTransacted() &&
                        !TransactionSynchronizationManager.isActualTransactionActive()) {
                    resourceHolder.setSynchronizedWithTransaction(true);
                    TransactionSynchronizationManager.bindResource(this.getConnectionFactory(),
                            resourceHolder);
                    boundHere = true;
                }
            } else {
                // if locally transacted, bind the current channel to make it available to RabbitTemplate
                if (isChannelLocallyTransacted()) {
                    RabbitResourceHolder localResourceHolder = new RabbitResourceHolder(channelToUse, false);
                    localResourceHolder.setSynchronizedWithTransaction(true);
                    TransactionSynchronizationManager.bindResource(this.getConnectionFactory(),
                            localResourceHolder);
                    boundHere = true;
                }
            }
            // Actually invoke the message listener...
            try {
                listener.onMessage(message, channelToUse);
            } catch (Exception e) {
                log.debug("MQ 消费失败 {}", message, e);
                MessageProperties properties = message.getMessageProperties();
                Map<String, Object> headers = properties.getHeaders();
                if (headers == null) {
                    headers = new HashMap<>();
                }
                headers.put("x-orig-routing-key", getOrigRoutingKey(properties, routingKey));
                int retryCount = headers.get("x-retry-times") == null ? 0 : (Integer) headers.get("x-retry-times");
                headers.put("x-retry-times", retryCount + 1);
                log.warn("[MQRetry] message {}, retry {}", new String(message.getBody(), "UTF-8"), retryCount);
                headers.put("x-exception-stacktrace", getStackTraceAsString(e));
                headers.put("x-exception-message", e.getCause() != null ? e.getCause().getMessage() : e.getMessage());

                if (retryCount >= retryTimes || e instanceof EventListenerNotFoundException) {
                    // 重试次数大于阈值，则自动加入到失败补偿队列
                    String failedExchangeName = exchangeName + ".FAILED";
                    channel.exchangeDeclare(failedExchangeName, BuiltinExchangeType.TOPIC);
                    channel.queueDeclare(queueName + ".FAILED", true, false, false, new HashMap<>());
                    channel.queueBind(queueName + ".FAILED", failedExchangeName, queueName);
                    channel.basicPublish(failedExchangeName, queueName, createOverrideProperties(properties, headers), message.getBody());
                } else {
                    // 重试次数小于阈值，则加入到重试队列
                    String retryExchangeName = exchangeName + ".RETRY";
                    channel.exchangeDeclare(retryExchangeName, BuiltinExchangeType.TOPIC);
                    channel.queueDeclare(queueName + ".RETRY", true, false, false, new HashMap<>());
                    channel.queueBind(queueName + ".RETRY", retryExchangeName, queueName);
                    channel.basicPublish(retryExchangeName, queueName, createOverrideProperties(properties, headers), message.getBody());
                }
            }
        } catch (Exception e) {
          throw new ListenerExecutionFailedException("MQ conusmer err", e, message);
        } finally {
            if (resourceHolder != null && boundHere) {
                // so the channel exposed (because exposeListenerChannel is false) will be closed
                resourceHolder.setSynchronizedWithTransaction(false);
            }
            ConnectionFactoryUtils.releaseResources(resourceHolder);
            if (boundHere) {
                // unbind if we bound
                TransactionSynchronizationManager.unbindResource(this.getConnectionFactory());
                if (!isExposeListenerChannel() && isChannelLocallyTransacted()) {
                    /*
                     *  commit the temporary channel we exposed; the consumer's channel
					 *  will be committed later. Note that when exposing a different channel
					 *  when there's no transaction manager, the exposed channel is committed
					 *  on each message, and not based on txSize.
					 */
                    RabbitUtils.commitIfNecessary(channelToUse);
                }
            }
        }
    }

    /**
     * 获取消息重试次数
     *
     * @param properties AMQP消息属性
     * @return 消息重试次数
     */
    protected Long getRetryCount(MessageProperties properties) {
        Long retryCount = 0L;
        try {
            Map<String, Object> headers = properties.getHeaders();
            if (headers != null) {
                if (headers.containsKey("x-death")) {
                    List<Map<String, Object>> deaths = (List<Map<String, Object>>) headers.get("x-death");
                    if (deaths.size() > 0) {
                        Map<String, Object> death = deaths.get(0);
                        retryCount = (Long) death.get("count");
                    }
                }
            }
        } catch (Exception ignored) {

        }

        return retryCount;
    }

    /**
     * 获取原始的routingKey
     *
     * @param properties   AMQP消息属性
     * @param defaultValue 默认值
     * @return 原始的routing-key
     */
    protected String getOrigRoutingKey(MessageProperties properties, String defaultValue) {
        String routingKey = defaultValue;
        try {
            Map<String, Object> headers = properties.getHeaders();
            if (headers != null) {
                if (headers.containsKey("x-orig-routing-key")) {
                    routingKey = headers.get("x-orig-routing-key").toString();
                }
            }
        } catch (Exception ignored) {
        }

        return routingKey;
    }

    /**
     * 从已有的properties中创建新的properties，使用提供的headers字段覆盖已有的headers
     *
     * @param properties AMQP属性
     * @param headers    要覆盖的headers
     * @return 新创建的properties
     */
    protected AMQP.BasicProperties createOverrideProperties(MessageProperties properties, Map<String, Object> headers) throws UnsupportedEncodingException {
        for (Map.Entry<String, Object> entry : headers.entrySet()) {
            properties.setHeader(entry.getKey(), entry.getValue());
        }
        return converter.fromMessageProperties(properties, "UTF-8");
    }

}
