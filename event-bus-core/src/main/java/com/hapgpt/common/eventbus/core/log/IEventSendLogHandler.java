package com.hapgpt.common.eventbus.core.log;

import com.hapgpt.common.eventbus.core.arg.EventSendLogModel;

public interface IEventSendLogHandler {
    void doHandler(EventSendLogModel eventLog);
}
