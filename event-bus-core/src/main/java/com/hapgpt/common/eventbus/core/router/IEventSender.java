package com.hapgpt.common.eventbus.core.router;

import com.hapgpt.common.eventbus.core.arg.EventObject;

public interface IEventSender {

    void push(EventObject eventObject);
}
