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
    BIZ_TYPE_NOT_EMPTY("10404", "bizType is empty")
    ;
    private String code;
    private String msg;
}
