package com.hapgpt.common.eventbus.mongo.configuration;

import com.hapgpt.common.eventbus.core.log.IEventSendLogHandler;
import com.hapgpt.common.eventbus.mongo.log.MongoEventSendLogHandler;
import org.springframework.context.annotation.Bean;

/**
 * @Author: kqyu
 * @Date: 2023/5/6 17:20
 * @Description:
 */
public class EventMongoAutoConfiguration {
    @Bean
    public IEventSendLogHandler eventSendLogHandler(){
        return new MongoEventSendLogHandler();
    }
}
