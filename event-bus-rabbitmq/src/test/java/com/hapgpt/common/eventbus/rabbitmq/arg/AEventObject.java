package com.hapgpt.common.eventbus.rabbitmq.arg;

import com.hapgpt.common.eventbus.core.arg.EventObject;

/**
 * grant
 * 7/5/2023 8:21 pm
 **/
public class AEventObject extends EventObject {
    public AEventObject() {
        bizType = "cc";
        bizId = System.currentTimeMillis() + "";
    }
}
