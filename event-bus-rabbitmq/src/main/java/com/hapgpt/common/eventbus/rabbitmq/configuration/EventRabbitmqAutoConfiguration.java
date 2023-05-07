package com.hapgpt.common.eventbus.rabbitmq.configuration;

import com.hapgpt.common.eventbus.core.router.IEventSender;
import com.hapgpt.common.eventbus.rabbitmq.router.RabbitmqEventSender;
import org.springframework.context.annotation.Bean;

/**
 * grant
 * 7/5/2023 7:31 pm
 **/
public class EventRabbitmqAutoConfiguration {

    @Bean
    public IEventSender eventSender() {
        return new RabbitmqEventSender();
    }
}
