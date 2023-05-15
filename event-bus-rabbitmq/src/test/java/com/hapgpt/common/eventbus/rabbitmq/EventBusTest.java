package com.hapgpt.common.eventbus.rabbitmq;

import com.hapgpt.common.eventbus.core.EventBus;
import com.hapgpt.common.eventbus.core.configuration.EventCoreAutoConfiguration;
import com.hapgpt.common.eventbus.rabbitmq.arg.AEventObject;
import com.hapgpt.common.eventbus.rabbitmq.arg.BEventObject;
import com.hapgpt.common.eventbus.rabbitmq.configuration.EventRabbitmqAutoConfiguration;
import com.hapgpt.common.eventbus.rabbitmq.listener.EventRabbitmqListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.EventListener;

import java.util.concurrent.TimeUnit;

/**
 * @Author: kqyu
 * @Date: 2023/5/6 14:52
 * @Description:
 */
@Slf4j
public class EventBusTest {

    AnnotationConfigApplicationContext applicationContext;

    @Before
    public void init() {
        applicationContext = new AnnotationConfigApplicationContext(EventCoreAutoConfiguration.class,
                EventRabbitmqAutoConfiguration.class, RabbitAutoConfiguration.class,
                EventBusTest.class, EventRabbitmqListener.class);
        applicationContext.start();
        applicationContext.registerShutdownHook();
    }

    @Test
    public void pushEvent() throws InterruptedException {
        AEventObject event = new AEventObject();
        applicationContext.publishEvent(new ApplicationStartedEvent(new SpringApplication(), new String[0], applicationContext));
//        applicationContext.getBean(EventBus.class).push(event);
        applicationContext.getBean(EventBus.class).push(new BEventObject());
        TimeUnit.SECONDS.sleep(15);
    }

    @EventListener
    public void sa(AEventObject eventObject) {
        throw new RuntimeException("sa");
    }
}
