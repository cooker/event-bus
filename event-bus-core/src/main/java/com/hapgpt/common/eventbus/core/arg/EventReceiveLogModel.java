package com.hapgpt.common.eventbus.core.arg;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * grant
 * 5/5/2023 10:11 am
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class EventReceiveLogModel extends BaseEventLogModel implements Serializable {

    private static final long serialVersionUID = 7742739835162381858L;

    /**
     * 事件消费应用名称
     */
    private String consumerName;
    /**
     * 事件消费状态
     */
    private EventStatusEnum status;
    /**
     * 执行日志
     */
    private List<EventMethodInvokeLog> invokeLogItems;

    public EventReceiveLogModel() {
        this.setCreateTime(System.currentTimeMillis());
        this.invokeLogItems = new ArrayList<>();
    }

    public EventReceiveLogModel(EventObject eventObject) {
        this();
        this.setEventId(eventObject.getEventId());
        this.setEventClass(eventObject.getClass().getName());
        this.setEventJson(JSON.toJSONString(eventObject));
        this.setBizType(eventObject.getBizType());
        this.setBizId(eventObject.getBizId());
    }

    public void setInvokeLogItems(List<EventMethodInvokeLog> invokeLogItems) {
        throw new UnsupportedOperationException();
    }

    public void addLogItem(EventMethodInvokeLog item){
        this.invokeLogItems.add(item);
    }
}
