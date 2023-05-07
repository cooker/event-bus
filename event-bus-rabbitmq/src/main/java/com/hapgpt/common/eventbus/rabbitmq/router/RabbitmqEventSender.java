package com.hapgpt.common.eventbus.rabbitmq.router;

import com.alibaba.fastjson.JSON;
import com.hapgpt.common.eventbus.core.arg.EventConstant;
import com.hapgpt.common.eventbus.core.arg.EventObject;
import com.hapgpt.common.eventbus.core.router.IEventSender;
import com.hapgpt.common.eventbus.rabbitmq.core.EventRabbitmqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * grant
 * 7/5/2023 7:40 pm
 **/
public class RabbitmqEventSender implements IEventSender {

    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RabbitAdmin rabbitAdmin;
    @Resource
    private Environment environment;
    @Value("${spring.profiles.active:dev}")
    private String env;

    @Override
    public void push(EventObject eventObject) {
        String topic = eventObject.get(EventRabbitmqConstant.BIZ_TOPIC);
        String routingKey = eventObject.get(EventRabbitmqConstant.BIZ_ROUTING_KEY);
        String exchange = eventObject.get(EventRabbitmqConstant.BIZ_EXCHANGE);
        if (StringUtils.isEmpty(routingKey)) {
            routingKey = "#";
        }
        if (StringUtils.isEmpty(topic)) {
            topic = env + "_" + environment.resolvePlaceholders(EventConstant.SPRING_APP_NAME);
        }
        if (StringUtils.isEmpty(exchange)) {
            exchange = eventObject.getClass().getName();
        }

        rabbitAdmin.declareExchange(new TopicExchange(exchange));
        rabbitAdmin.declareQueue(new Queue(topic));
        rabbitAdmin.declareBinding(new Binding(topic, Binding.DestinationType.EXCHANGE, exchange, routingKey, new HashMap<>()));
        rabbitTemplate.convertAndSend(exchange, routingKey, JSON.toJSONString(eventObject));
    }
}
