package com.hapgpt.common.eventbus.core.extend;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

/**
 * 默认eventId 生成器
 * grant
 * 5/5/2023 8:57 am
 **/
public class EventIdGenerator implements IEventIdGenerator {
    @Override
    public String nextId() {
        return System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replaceAll("-", "").substring(13);
    }

    @Override
    public Long getTimestamp(String id) {
        return Long.parseLong(id.substring(0,13));
    }

    @Override
    public LocalDateTime getLocalDateTime(String id) {
        return Instant.ofEpochMilli(getTimestamp(id)).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
