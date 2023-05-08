package com.hapgpt.common.eventbus.core.arg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * grant
 * 5/5/2023 6:43 am
 **/
@Getter
@Setter
@ToString
public abstract class EventObject extends ApplicationEvent implements Serializable {

    private static final long serialVersionUID = -3627285794800801992L;
    /**
     * 业务类型
     */
    protected String bizType;
    /**
     * 业务id
     */
    protected String bizId;
    /**
     * 事件唯一id
     */
    protected String eventId;
    /**
     * 事件时间(ms)
     */
    protected Long eventTime;
    /**
     * 事件头
     */
    protected Map<String, String> headers;

    public EventObject() {
        super("");
        this.eventTime = System.currentTimeMillis();
        this.headers = new HashMap<>();
        this.headers.put(EventConstant.BIZ_CLASS, this.getClass().getName());
    }

    public void put(String key, String value) {
        headers.put(key, value);
    }

    public String get(String key) {
        return headers.get(key);
    }
}
