package com.hapgpt.common.eventbus.core.extend;

import org.springframework.core.Ordered;

import java.util.Map;

/**
 * grant
 * 5/5/2023 8:19 am
 **/
public interface IEventConsumerInterceptor extends Ordered {
    void intercept(String paloadJson, Map<String, String> headers);

    default int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
