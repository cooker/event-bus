package com.hapgpt.common.eventbus.core.log;

import com.hapgpt.common.eventbus.core.arg.EventReceiveLogModel;

public interface IEventReceiveLogHandler {
    void doHandler(EventReceiveLogModel eventLog);
}
