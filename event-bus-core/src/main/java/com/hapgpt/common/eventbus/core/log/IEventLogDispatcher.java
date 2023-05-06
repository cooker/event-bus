package com.hapgpt.common.eventbus.core.log;

import com.hapgpt.common.eventbus.core.arg.BaseEventLogModel;

public interface IEventLogDispatcher {
    /**
     * 发送事件日志
     *
     * @param eventLog 事件日志
     */
    void dispatch(BaseEventLogModel eventLog);

    void shutdown();
}
