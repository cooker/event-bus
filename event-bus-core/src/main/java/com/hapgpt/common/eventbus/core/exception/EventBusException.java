package com.hapgpt.common.eventbus.core.exception;

import lombok.Getter;

/**
 * @Author: kqyu
 * @Date: 2023/4/28 12:08
 * @Description:
 */
@Getter
public class EventBusException extends RuntimeException {
    private String code;
    private String message;

    public EventBusException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public EventBusException(EventBusErrorEnum val) {
        this.code = val.getCode();
        this.message = val.getMsg();
    }
}
