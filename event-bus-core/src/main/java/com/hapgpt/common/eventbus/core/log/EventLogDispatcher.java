package com.hapgpt.common.eventbus.core.log;

import com.hapgpt.common.eventbus.core.arg.BaseEventLogModel;
import com.hapgpt.common.eventbus.core.arg.EventReceiveLogModel;
import com.hapgpt.common.eventbus.core.arg.EventSendLogModel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * grant
 * 5/5/2023 10:46 am
 **/
@Slf4j
@AllArgsConstructor
public class EventLogDispatcher implements IEventLogDispatcher {

    private final IEventSendLogHandler eventSendLogHandler;
    private final IEventReceiveLogHandler eventReceiveLogHandler;

    @Override
    public void dispatch(BaseEventLogModel eventLog) {
        if (eventLog instanceof EventSendLogModel) {
            if (eventSendLogHandler == null) {
                log.debug("eventSendLogHandler not found >> {}", eventLog.getEventId());
            } else {
                eventSendLogHandler.doHandler((EventSendLogModel)eventLog);
            }
        } else if (eventLog instanceof EventReceiveLogModel) {
            if (eventReceiveLogHandler == null) {
                log.debug("eventReceiveLogHandler not found >> {}", eventLog.getEventId());
            } else {
                eventReceiveLogHandler.doHandler((EventReceiveLogModel)eventLog);
            }
        } else {
            log.debug("LogHandler not match >> {}", eventLog.getEventId());
        }
    }

    @Override
    public void shutdown() {
        log.info("EventLogDispatcher shutdown");
    }
}
