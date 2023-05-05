package com.hapgpt.common.eventbus.core.configuration;

import com.hapgpt.common.eventbus.core.EventBus;
import com.hapgpt.common.eventbus.core.extend.EventIdGenerator;
import com.hapgpt.common.eventbus.core.extend.IEventIdGenerator;
import com.hapgpt.common.eventbus.core.router.EventConsumer;
import com.hapgpt.common.eventbus.core.router.EventSender;
import com.hapgpt.common.eventbus.core.router.IEventConsumer;
import com.hapgpt.common.eventbus.core.extend.IEventSendInterceptor;
import com.hapgpt.common.eventbus.core.router.IEventSender;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.Environment;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * grant
 * 5/5/2023 7:06 am
 **/
@Configuration
public class EventCoreAutoConfiguration {












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
                             ObjectProvider<Collection<IEventSendInterceptor>> eventSendInterceptors) {
        Collection<IEventSendInterceptor> eventSendInterceptorList = eventSendInterceptors.getIfAvailable();
        if (eventSendInterceptorList != null) {
            eventSendInterceptorList = eventSendInterceptorList.stream().sorted(AnnotationAwareOrderComparator.INSTANCE).collect(Collectors.toList());
        }
        return new EventBus(environment, eventIdGenerator.getIfAvailable(),
                eventSender.getIfAvailable(), eventConsumer.getIfAvailable(),
                eventSendInterceptorList);
    }
}
