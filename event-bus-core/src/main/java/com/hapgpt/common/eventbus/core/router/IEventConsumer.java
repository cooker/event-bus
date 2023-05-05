package com.hapgpt.common.eventbus.core.router;

import com.hapgpt.common.eventbus.core.arg.EventObject;

public interface IEventConsumer {

    void receive(EventObject eventObject);

    void receiveJson(String json);
}
