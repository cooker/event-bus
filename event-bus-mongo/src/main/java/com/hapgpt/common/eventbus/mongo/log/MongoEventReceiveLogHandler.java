package com.hapgpt.common.eventbus.mongo.log;

import com.hapgpt.common.eventbus.core.arg.EventReceiveLogModel;
import com.hapgpt.common.eventbus.core.extend.IEventIdGenerator;
import com.hapgpt.common.eventbus.core.log.IEventReceiveLogHandler;
import com.hapgpt.common.eventbus.mongo.core.EventMongoConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: kqyu
 * @Date: 2023/5/6 17:25
 * @Description:
 */
public class MongoEventReceiveLogHandler implements IEventReceiveLogHandler {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IEventIdGenerator eventIdGenerator;

    @Override
    public void doHandler(EventReceiveLogModel eventLog) {
        mongoTemplate.insert(eventLog, getCollectionByEventId(eventLog.getEventId()));
    }

    public static String getCollectionByEventId(String eventId) {
        LocalDateTime localDateTime = eventIdGenerator.getLocalDateTime(eventId);
        return EventMongoConstant.RECEIVE_LOG_COLLECTION + "_" + localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
