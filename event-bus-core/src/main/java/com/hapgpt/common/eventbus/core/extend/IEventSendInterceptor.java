package com.hapgpt.common.eventbus.core.extend;

import com.hapgpt.common.eventbus.core.arg.EventObject;
import com.hapgpt.common.eventbus.core.router.IEventSender;
import org.springframework.core.Ordered;

/**
 * 事件注册拦截器，事件发送前对事件的处理
 * grant
 * 5/5/2023 8:19 am
 **/
public interface IEventSendInterceptor extends Ordered {
    /**
     * 事件发送拦截处理方法
     * @param eventObject 事件参数
     * @param eventSender 发送器
     */
    void intercept(EventObject eventObject, IEventSender eventSender);

    default int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
