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

    /**
     * 默认消费者数
     */
    private Integer coreThread = 1;
    /**
     * 默认重试次数
     */
    private Integer coreRetryTimes = 3;


    @Data
    public static class RabbitmqConsumer {
        private Integer thread;
        private Integer reetryTimes = 3;
        private String exchangeName;
        private String queueName;
        private String routingKey;
    }
}
