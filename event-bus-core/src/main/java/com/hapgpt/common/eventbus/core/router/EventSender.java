package com.hapgpt.common.eventbus.core.router;

import com.hapgpt.common.eventbus.core.arg.EventObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 事件发射器
 * grant
 * 5/5/2023 9:08 am
 **/
@Slf4j
public class EventSender implements IEventSender {

    @Autowired
    private IEventConsumer eventConsumer;

    @Override
    public void push(EventObject eventObject) {
        log.debug("转发消息 ==> {}", eventObject.getEventId());
        eventConsumer.receive(eventObject);
    }
}
