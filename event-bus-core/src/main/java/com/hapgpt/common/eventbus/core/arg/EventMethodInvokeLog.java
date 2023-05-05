package com.hapgpt.common.eventbus.core.arg;

import lombok.Data;

import java.io.Serializable;

/**
 * grant
 * 5/5/2023 4:23 pm
 **/
@Data
public class EventMethodInvokeLog implements Serializable {

    private static final long serialVersionUID = -8342004931708657470L;
    /**
     * 是否已经执行过
     */
    private Boolean isExecuted;
    /**
     * 是否执行成功
     */
    private Boolean isSuccess;
    /**
     * 错误信息
     */
    private String errMsg;
    /**
     * 监听类名称
     */
    private String clazz;
    /**
     * 事件处理方法
     */
    private String method;
    /**
     * 开始时间
     */
    private Long startTime;
    /**
     * 执行时间
     */
    private Long processTime;
    /**
     * 结束时间
     */
    private Long endTime;
}
