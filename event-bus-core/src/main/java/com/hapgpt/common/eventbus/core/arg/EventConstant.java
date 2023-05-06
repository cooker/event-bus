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
     * 异常堆栈大小（默认1024）
     */
    String ENV_STACK_DEPTH = "event.bus.stack.depth";
}
