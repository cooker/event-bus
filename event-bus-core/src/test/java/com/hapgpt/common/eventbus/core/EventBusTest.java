package com.hapgpt.common.eventbus.core;

import com.hapgpt.common.eventbus.core.arg.AEventObject;
import com.hapgpt.common.eventbus.core.arg.EventConstant;
import com.hapgpt.common.eventbus.core.config.EventReceiveLogHandlerConfig;
import com.hapgpt.common.eventbus.core.configuration.EventCoreAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

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
                EventReceiveLogHandlerConfig.class, EventBusTest.class);
        applicationContext.registerShutdownHook();
    }

    @Test
    public void pushEvent() throws InterruptedException {
        AEventObject event = new AEventObject();
        event.put(EventConstant.BIZ_LOCAL, "");
        applicationContext.getBean(EventBus.class).push(event);
        TimeUnit.SECONDS.sleep(5);
    }

    @EventListener
    public void ac(AEventObject eventObject) {
        log.info("处理消息：{}", eventObject);
        throw new RuntimeException("ccc");
    }

}
