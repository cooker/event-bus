package com.hapgpt.common.eventbus.core.configuration;

import com.hapgpt.common.eventbus.core.EventBus;
import com.hapgpt.common.eventbus.core.aop.SpringEventListenerAspect;
import com.hapgpt.common.eventbus.core.extend.EventIdGenerator;
import com.hapgpt.common.eventbus.core.extend.IEventIdGenerator;
import com.hapgpt.common.eventbus.core.extend.IEventSendInterceptor;
import com.hapgpt.common.eventbus.core.log.EventLogDispatcher;
import com.hapgpt.common.eventbus.core.log.IEventLogDispatcher;
import com.hapgpt.common.eventbus.core.log.IEventReceiveLogHandler;
import com.hapgpt.common.eventbus.core.log.IEventSendLogHandler;
import com.hapgpt.common.eventbus.core.router.EventConsumer;
import com.hapgpt.common.eventbus.core.router.EventSender;
import com.hapgpt.common.eventbus.core.router.IEventConsumer;
import com.hapgpt.common.eventbus.core.router.IEventSender;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.Environment;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * grant
 * 5/5/2023 7:06 am
 **/
@Configuration
@EnableAspectJAutoProxy
public class EventCoreAutoConfiguration {

    @Bean
    public SpringEventListenerAspect springEventListenerAop() {
        return new SpringEventListenerAspect();
    }

    @Bean(destroyMethod = "shutdown")
    public IEventLogDispatcher eventLogDispatcher(ObjectProvider<IEventSendLogHandler> eventSendLogHandler,
                                                  ObjectProvider<IEventReceiveLogHandler> eventReceiveLogHandler) {
        return new EventLogDispatcher(eventSendLogHandler.getIfAvailable(), eventReceiveLogHandler.getIfAvailable());
    }

    @Bean
    @ConditionalOnMissingBean(IEventIdGenerator.class)
    public IEventIdGenerator eventIdGenerator() {
        return new EventIdGenerator();
    }

    @Bean
    @ConditionalOnMissingBean(IEventSender.class)
    public IEventSender eventSender() {
        return new EventSender();
    }

    @Bean
    @ConditionalOnMissingBean(IEventConsumer.class)
    public IEventConsumer eventConsumer() {
        return new EventConsumer();
    }

    @Bean
    public EventBus eventBus(Environment environment, ObjectProvider<IEventIdGenerator> eventIdGenerator,
                             ObjectProvider<IEventSender> eventSender, ObjectProvider<IEventConsumer> eventConsumer,
                             ObjectProvider<Collection<IEventSendInterceptor>> eventSendInterceptors,
                             ObjectProvider<IEventLogDispatcher> eventLogDispatcher) {
        Collection<IEventSendInterceptor> eventSendInterceptorList = eventSendInterceptors.getIfAvailable();
        if (eventSendInterceptorList != null) {
            eventSendInterceptorList = eventSendInterceptorList.stream().sorted(AnnotationAwareOrderComparator.INSTANCE).collect(Collectors.toList());
        }
        return new EventBus(environment, eventIdGenerator.getIfAvailable(),
                eventSender.getIfAvailable(), eventConsumer.getIfAvailable(),
                eventSendInterceptorList, eventLogDispatcher.getIfAvailable());
    }
}
