package com.hapgpt.common.eventbus.core.exception;

/**
 * @Author: kqyu
 * @Date: 2023/5/10 11:46
 * @Description: 找不到监听类
 */
public class EventListenerNotFoundException extends RuntimeException {
    public EventListenerNotFoundException(String message) {
        super(message);
    }
}
