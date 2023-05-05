package com.hapgpt.common.eventbus.core.router;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hapgpt.common.eventbus.core.arg.EventConstant;
import com.hapgpt.common.eventbus.core.arg.EventObject;
import com.hapgpt.common.eventbus.core.exception.EventBusErrorEnum;
import com.hapgpt.common.eventbus.core.exception.EventBusException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

/**
 * 事件消费者
 * grant
 * 5/5/2023 7:42 am
 **/
@Slf4j
public class EventConsumer implements IEventConsumer, ApplicationEventPublisherAware {
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    public void receiveJson(String json) {
        log.debug("消费 ==> {}", json);
        JSONObject headers = JSON.parseObject(json).getJSONObject("headers");
        String className = headers.getString(EventConstant.BIZ_CLASS);
        try {
            Class cl = Class.forName(className);
            Object obj = JSON.parseObject(json, cl);
            this.receive((EventObject) obj);
        } catch (ClassNotFoundException e) {
            throw new EventBusException(EventBusErrorEnum.BIZ_CLASS_NOT_FOUND).detailMsg("Not Found + " + className);
        }
    }

    public void receive(EventObject eventObject) {
        eventPublisher.publishEvent(eventObject);
    }
}
