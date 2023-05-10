package com.hapgpt.common.eventbus.rabbitmq.arg;

import com.hapgpt.common.eventbus.core.arg.EventObject;

/**
 * grant
 * 7/5/2023 8:21 pm
 **/
public class BEventObject extends EventObject {
    public BEventObject() {
        bizType = "cc";
        bizId = System.currentTimeMillis() + "";
    }
}
