package com.hapgpt.common.eventbus.rabbitmq;

import com.hapgpt.common.eventbus.core.EventBus;
import com.hapgpt.common.eventbus.core.configuration.EventCoreAutoConfiguration;
import com.hapgpt.common.eventbus.rabbitmq.arg.AEventObject;
import com.hapgpt.common.eventbus.rabbitmq.config.RabbitConfig;
import com.hapgpt.common.eventbus.rabbitmq.configuration.EventRabbitmqAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * @Author: kqyu
 * @Date: 2023/5/6 14:52
 * @Description:
 */
@Slf4j
@Configuration
public class EventBusTest {

    AnnotationConfigApplicationContext applicationContext;

    @Before
    public void init() {
        applicationContext = new AnnotationConfigApplicationContext(EventCoreAutoConfiguration.class,
                EventRabbitmqAutoConfiguration.class, RabbitAutoConfiguration.class,
                EventBusTest.class);
        applicationContext.registerBean(PropertySourcesPlaceholderConfigurer.class, BeanDefinitionBuilder.genericBeanDefinition(PropertySourcesPlaceholderConfigurer.class));
        applicationContext.registerShutdownHook();
    }

    @Test
    public void pushEvent() throws InterruptedException {
        AEventObject event = new AEventObject();

        applicationContext.getBean(EventBus.class).push(event);
        TimeUnit.SECONDS.sleep(5);
    }

}
