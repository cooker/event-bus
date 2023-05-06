package com.hapgpt.common.eventbus.core.arg;

public interface EventConstant {
    /**
     * 事件子类
     */
    String BIZ_CLASS = "@eventClazz";
    /**
     * 是否本地事件
     */
    String BIZ_LOCAL = "@eventLocal";
    /**
     * 重推
     */
    String BIZ_RETRY = "@eventRetryModel";


    /**
     * 绑定 topic（默认 [环境]-[应用名]）
     */
    String BIZ_TOPIC = "@topic";
    /**
     * 绑定 exchange (默认 [事件类名])
     */
    String BIZ_EXCHANGE = "@exchange";

    /**
     * 异常堆栈大小（默认1024）
     */
    String ENV_STACK_DEPTH = "event.bus.stack.depth";

}
