package com.hapgpt.common.eventbus.rabbitmq.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * grant
 * 7/5/2023 8:37 pm
 **/
@Data
@ConfigurationProperties(prefix = "event.bus.rabbit")
public class EventRabbitmqProperties {
    List<RabbitmqConsumer> consumers;



    @Data
    public static class RabbitmqConsumer {
        private Integer thread;
        private String queueName;
    }
}
