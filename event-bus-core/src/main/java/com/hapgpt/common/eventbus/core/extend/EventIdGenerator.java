package com.hapgpt.common.eventbus.core.extend;

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
}
