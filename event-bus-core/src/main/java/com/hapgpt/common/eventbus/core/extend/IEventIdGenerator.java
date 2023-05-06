package com.hapgpt.common.eventbus.core.extend;

import java.time.LocalDateTime;

public interface IEventIdGenerator {
    String nextId();

    Long getTimestamp(String id);

    LocalDateTime getLocalDateTime(String id);
}
