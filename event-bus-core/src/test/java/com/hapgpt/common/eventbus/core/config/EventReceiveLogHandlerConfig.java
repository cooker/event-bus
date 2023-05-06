package com.hapgpt.common.eventbus.core.config;

import com.alibaba.fastjson.JSON;
import com.hapgpt.common.eventbus.core.arg.EventReceiveLogModel;
import com.hapgpt.common.eventbus.core.arg.EventSendLogModel;
import com.hapgpt.common.eventbus.core.log.IEventReceiveLogHandler;
import com.hapgpt.common.eventbus.core.log.IEventSendLogHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: kqyu
 * @Date: 2023/5/6 15:37
 * @Description:
 */
@Slf4j
@Configuration
public class EventReceiveLogHandlerConfig {

    @Bean
    public IEventReceiveLogHandler eventReceiveLogHandler() {
        return new IEventReceiveLogHandler() {
            @Override
            public void doHandler(EventReceiveLogModel eventLog) {
                log.info("接收记录：{}", JSON.toJSONString(eventLog));
            }
        };
    }

    @Bean
    public IEventSendLogHandler eventSendLogHandler() {
        return new IEventSendLogHandler() {
            @Override
            public void doHandler(EventSendLogModel eventLog) {
                log.info("发送记录：{}", JSON.toJSONString(eventLog));
            }
        };
    }
}
