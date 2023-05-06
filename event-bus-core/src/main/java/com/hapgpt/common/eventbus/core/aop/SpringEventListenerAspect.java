package com.hapgpt.common.eventbus.core.aop;

import com.alibaba.fastjson.JSON;
import com.hapgpt.common.eventbus.core.arg.EventConstant;
import com.hapgpt.common.eventbus.core.arg.EventMethodInvokeLog;
import com.hapgpt.common.eventbus.core.arg.EventObject;
import com.hapgpt.common.eventbus.core.arg.EventReceiveLogModel;
import com.hapgpt.common.eventbus.core.arg.EventStatusEnum;
import com.hapgpt.common.eventbus.core.log.IEventLogDispatcher;
import com.hapgpt.common.eventbus.core.utils.Tools;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @Author: grant
 * @Date: 2023/5/6 14:08
 * @Description: spring @eventListener 注解拦截器
 */
@Slf4j
@Aspect
public class SpringEventListenerAspect {

    @Autowired
    private IEventLogDispatcher eventLogDispatcher;
    @Autowired
    private Environment environment;

    @Around("@annotation(org.springframework.context.event.EventListener) && args(arg)")
    public Object exec(ProceedingJoinPoint joinPoint, EventObject arg) throws Throwable {
        //处理
        String retryModelJson = arg.get(EventConstant.BIZ_RETRY);
        EventReceiveLogModel receiveLog = null;
        if (!StringUtils.isEmpty(retryModelJson)) {
            receiveLog = JSON.parseObject(retryModelJson, EventReceiveLogModel.class);
        } else {
            receiveLog = new EventReceiveLogModel(arg);
            receiveLog.setConsumerName(environment.resolvePlaceholders("${spring.application.name:app}"));
        }
        EventMethodInvokeLog methodInvokeLog = new EventMethodInvokeLog();
        methodInvokeLog.setIsExecuted(true);
        methodInvokeLog.setIsSuccess(true);
        methodInvokeLog.setErrMsg("");
        Class<?> targetClazz = ClassUtils.getUserClass(joinPoint.getTarget().getClass());//修复：cglib 导致无法获取目标class
        methodInvokeLog.setClazz(targetClazz.getName());
        MethodSignature ms = (MethodSignature)joinPoint.getSignature();
        Method targetMethod = targetClazz.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
        methodInvokeLog.setMethod(targetMethod.getName());
        methodInvokeLog.setSTime(System.currentTimeMillis());
        receiveLog.addLogItem(methodInvokeLog);
        try {
            Object ret = joinPoint.proceed();
            receiveLog.setStatus(EventStatusEnum.SUCCESS);
            return ret;
        } catch (Throwable e) {
            receiveLog.setStatus(EventStatusEnum.FAIL);
            methodInvokeLog.setIsSuccess(false);
            methodInvokeLog.setErrMsg(Tools.unwrapThrowable(e));
            throw e;
        } finally {
            methodInvokeLog.setETime(System.currentTimeMillis());
            methodInvokeLog.setPTime(methodInvokeLog.getETime() - methodInvokeLog.getSTime());
            eventLogDispatcher.dispatch(receiveLog);
        }
    }
}
