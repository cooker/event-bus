package com.hapgpt.common.eventbus.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: kqyu
 * @Date: 2023/4/28 17:58
 * @Description:
 */
@Getter
@AllArgsConstructor
public enum EventBusErrorEnum {
    BIZ_TYPE_NOT_EMPTY("10404", "bizType is empty"),
    BIZ_ID_NOT_EMPTY("10405", "bizId is empty"),
    BIZ_SENDER_NOT_EMPTY("10406", "bizSender is empty"),
    BIZ_CLASS_NOT_FOUND("10407", "event class not found"),
    ;
    private String code;
    private String msg;
}
