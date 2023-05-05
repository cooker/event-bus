package com.hapgpt.common.eventbus.core.arg;

import lombok.Data;

import java.io.Serializable;

/**
 * grant
 * 5/5/2023 10:03 am
 **/
@Data
public abstract class BaseEventLogModel implements Serializable {

    private static final long serialVersionUID = 6664619485450160506L;
    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 业务id
     */
    private String bizId;
    /**
     * 事件id
     */
    private String eventId;
    /**
     * 事件类
     */
    private String eventClass;
    /**
     * 事件json数据
     */
    private String eventJson;
    /**
     * 事件创建时间（发生事件）
     */
    private Long createTime;
    /**
     * 重试次数
     */
    private Integer retryCount;
}
