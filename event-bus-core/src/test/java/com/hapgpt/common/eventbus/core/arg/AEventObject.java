package com.hapgpt.common.eventbus.core.arg;

/**
 * @Author: kqyu
 * @Date: 2023/5/6 14:57
 * @Description:
 */
public class AEventObject extends EventObject {
    public AEventObject() {
        bizType = "cc";
        bizId = System.currentTimeMillis() + "";
    }
}
