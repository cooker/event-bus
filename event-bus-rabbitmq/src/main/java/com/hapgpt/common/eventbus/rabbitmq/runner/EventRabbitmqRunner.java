package com.hapgpt.common.eventbus.rabbitmq.runner;

import com.hapgpt.common.eventbus.core.arg.EventConstant;
import com.hapgpt.common.eventbus.core.router.IEventConsumer;
import com.hapgpt.common.eventbus.rabbitmq.router.RetryConsumerContainer;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;

import javax.annotation.Resource;

/**
 * @Author: kqyu
 * @Date: 2023/5/8 17:59
 * @Description:
 */
@Slf4j
public class EventRabbitmqRunner implements ApplicationListener<ApplicationStartedEvent> {

    static volatile boolean isExec = false;
    @Resource
    private Environment environment;
    @Value("${spring.profiles.active:dev}")
    private String env;
    @Resource
    private ConnectionFactory connectionFactory;
    @Resource
    private IEventConsumer eventConsumer;


    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        if (!isExec) {
            String queue = env + "_" + environment.resolvePlaceholders(EventConstant.SPRING_APP_NAME);
            RetryConsumerContainer retryConsumerContainer = new RetryConsumerContainer(connectionFactory, "", queue, "", 3);
            retryConsumerContainer.setQueues(new Queue(queue));
            retryConsumerContainer.setChannelAwareMessageListener(new ChannelAwareMessageListener() {
                @Override
                public void onMessage(Message message, Channel channel) throws Exception {
                    String json = new String(message.getBody());
                    eventConsumer.receiveJson(json);
                    channel.basicAck(message.getMessageProperties().gec);
                }
            });
            retryConsumerContainer.start();
            log.info("启动MQ默认消费者：{}", queue);
            isExec = true;
        }
    }
}
