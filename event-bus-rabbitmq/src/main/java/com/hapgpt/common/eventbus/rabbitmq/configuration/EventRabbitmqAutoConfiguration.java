package com.hapgpt.common.eventbus.rabbitmq.configuration;

import com.hapgpt.common.eventbus.core.router.IEventSender;
import com.hapgpt.common.eventbus.rabbitmq.core.EventRabbitmqProperties;
import com.hapgpt.common.eventbus.rabbitmq.router.RabbitmqEventSender;
import com.hapgpt.common.eventbus.rabbitmq.router.RetryConsumerContainer;
import com.hapgpt.common.eventbus.rabbitmq.runner.EventRabbitmqRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * grant
 * 7/5/2023 7:31 pm
 **/
@EnableConfigurationProperties(EventRabbitmqProperties.class)
public class EventRabbitmqAutoConfiguration {

    @Bean
    public IEventSender eventSender() {
        return new RabbitmqEventSender();
    }

    @Bean
    public EventRabbitmqRunner eventRabbitmqRunner() {
        return new EventRabbitmqRunner();
    }
}
