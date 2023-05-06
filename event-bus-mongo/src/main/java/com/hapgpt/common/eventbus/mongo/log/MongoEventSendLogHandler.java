package com.hapgpt.common.eventbus.mongo.log;

import com.hapgpt.common.eventbus.core.arg.EventSendLogModel;
import com.hapgpt.common.eventbus.core.extend.IEventIdGenerator;
import com.hapgpt.common.eventbus.core.log.IEventSendLogHandler;
import com.hapgpt.common.eventbus.mongo.core.EventMongoConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @Author: kqyu
 * @Date: 2023/5/6 17:21
 * @Description:
 */
public class MongoEventSendLogHandler implements IEventSendLogHandler {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IEventIdGenerator eventIdGenerator;
    @Override
    public void doHandler(EventSendLogModel eventLog) {
        mongoTemplate.insert(eventLog, getCollectionByEventId(eventLog.getEventId()));
    }

    public static String getCollectionByEventId(String eventId) {
        LocalDateTime localDateTime = eventIdGenerator.getLocalDateTime(eventId);
        return EventMongoConstant.SEND_LOG_COLLECTION + "_" + localDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
