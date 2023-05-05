package com.hapgpt.common.eventbus.core.arg;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * grant
 * 5/5/2023 10:11 am
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class EventSendLogModel extends BaseEventLogModel implements Serializable {

    private static final long serialVersionUID = -2785427028180236746L;

    /**
     * 事件发送应用名称
     */
    private String producerName;
    /**
     * 事件发送状态
     */
    private String status;

    public EventSendLogModel() {
        this.setCreateTime(System.currentTimeMillis());
        this.setStatus(EventConstant.STATUS_SUCCESS);
    }

    public EventSendLogModel(EventObject eventObject) {
        this();
        this.setEventId(eventObject.getEventId());
        this.setEventClass(eventObject.getClass().getName());
        this.setEventJson(JSON.toJSONString(eventObject));
        if (eventObject.getEventTime() != null) {
            this.setCreateTime(eventObject.getEventTime());
        }
        this.setBizType(eventObject.getBizType());
        this.setBizId(eventObject.getBizId());
    }
}
