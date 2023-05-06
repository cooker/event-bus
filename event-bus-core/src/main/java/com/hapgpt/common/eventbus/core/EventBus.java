package com.hapgpt.common.eventbus.core;

import com.alibaba.fastjson.JSON;
import com.hapgpt.common.eventbus.core.arg.EventConstant;
import com.hapgpt.common.eventbus.core.arg.EventObject;
import com.hapgpt.common.eventbus.core.arg.EventSendLogModel;
import com.hapgpt.common.eventbus.core.arg.EventStatusEnum;
import com.hapgpt.common.eventbus.core.exception.EventBusErrorEnum;
import com.hapgpt.common.eventbus.core.exception.EventBusException;
import com.hapgpt.common.eventbus.core.extend.IEventIdGenerator;
import com.hapgpt.common.eventbus.core.log.IEventLogDispatcher;
import com.hapgpt.common.eventbus.core.router.IEventConsumer;
import com.hapgpt.common.eventbus.core.extend.IEventSendInterceptor;
import com.hapgpt.common.eventbus.core.router.IEventSender;
import com.hapgpt.common.eventbus.core.utils.Tools;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * grant
 * 5/5/2023 7:28 am
 **/
@Slf4j
@Getter
@AllArgsConstructor
public class EventBus {
    private final Environment environment;
    private final IEventIdGenerator eventIdGenerator;
    private final IEventSender eventSender;
    private final IEventConsumer eventConsumer;
    private final Collection<IEventSendInterceptor> eventSendInterceptors;
    private final IEventLogDispatcher eventLogDispatcher;

    public void push(EventObject eventObject) {
        if (StringUtils.isEmpty(eventObject.getBizType())) {
            throw new EventBusException(EventBusErrorEnum.BIZ_TYPE_NOT_EMPTY);
        }

        if (StringUtils.isEmpty(eventObject.getBizId())) {
            throw new EventBusException(EventBusErrorEnum.BIZ_ID_NOT_EMPTY);
        }
        //设置eventId
        eventObject.setEventId(eventIdGenerator.nextId());
        EventSendLogModel sendLog = new EventSendLogModel(eventObject);
        sendLog.setProducerName(environment.resolvePlaceholders("${spring.application.name:app}"));
        try {
            if (eventSendInterceptors != null) {
                for (IEventSendInterceptor interceptor : eventSendInterceptors) {
                    interceptor.intercept(eventObject, eventSender);
                }
            }

            if (eventObject.getHeaders().containsKey(EventConstant.BIZ_LOCAL)) {
                log.debug("本地消息 ==> {}", JSON.toJSONString(eventObject));
                eventConsumer.receive(eventObject);
            } else {
                if (eventSender == null) {
                    throw new EventBusException(EventBusErrorEnum.BIZ_SENDER_NOT_EMPTY);
                }
                log.debug("远程消息 ==> {}", JSON.toJSONString(eventObject));
                eventSender.push(eventObject);
            }
        } catch (Exception e) {
            sendLog.setStatus(EventStatusEnum.FAIL);
            sendLog.setErrMsg(Tools.unwrapThrowable(e));
            throw e;
        } finally {
            //日志记录
            eventLogDispatcher.dispatch(sendLog);
        }
    }

    //TODO
    public void retryPush(String eventId){
//        List<EventHandleLog> eventHandleLogs =  eventHandleLogStore.getHandleLogList(eventId);
//        if(CollectionUtils.isEmpty(eventHandleLogs)){
//            log.error("事件{}重试失败！未找到事件id{}",eventId,eventId);
//            throw new RuntimeException("事件"+eventId+"重试失败！");
//        }
//        for (EventHandleLog eventHandleLog: eventHandleLogs){
//            eventSender.retryHandle(eventHandleLog);
//        }
    }
}
