package com.hapgpt.common.eventbus.rabbitmq.core;

public interface EventRabbitmqConstant {

    /**
     * 绑定 topic（默认 [环境]-[应用名]）
     */
    String BIZ_TOPIC = "@topic";

    String BIZ_ROUTING_KEY = "@routingKey";
    /**
     * 绑定 exchange (默认 [事件类名])
     */
    String BIZ_EXCHANGE = "@exchange";
}
