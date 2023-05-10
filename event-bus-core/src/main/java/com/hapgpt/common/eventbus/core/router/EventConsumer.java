package com.hapgpt.common.eventbus.core.router;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hapgpt.common.eventbus.core.arg.EventConstant;
import com.hapgpt.common.eventbus.core.arg.EventObject;
import com.hapgpt.common.eventbus.core.exception.EventBusErrorEnum;
import com.hapgpt.common.eventbus.core.exception.EventBusException;
import com.hapgpt.common.eventbus.core.exception.EventListenerNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.AbstractApplicationEventMulticaster;
import org.springframework.core.ResolvableType;

import java.lang.reflect.Method;
import java.util.Collection;

/**
 * 事件消费者
 * grant
 * 5/5/2023 7:42 am
 **/
@Slf4j
public class EventConsumer implements IEventConsumer, ApplicationEventPublisherAware, ApplicationContextAware {
    private ApplicationEventPublisher eventPublisher;
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
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

    @SneakyThrows
    public void receive(EventObject eventObject) {
        AbstractApplicationEventMulticaster multicaster = applicationContext.getBean(AbstractApplicationEventMulticaster.class);
        Method method = AbstractApplicationEventMulticaster.class.getDeclaredMethod("getApplicationListeners", ApplicationEvent.class, ResolvableType.class);
        method.setAccessible(true);
        Collection<ApplicationListener<?>> listeners = (Collection<ApplicationListener<?>>) method.invoke(multicaster, eventObject, ResolvableType.forInstance(eventObject));
        if (listeners.isEmpty()) {
            throw new EventListenerNotFoundException(eventObject.getClass().getName() + " listener not found");
        }
        eventPublisher.publishEvent(eventObject);
    }
}
