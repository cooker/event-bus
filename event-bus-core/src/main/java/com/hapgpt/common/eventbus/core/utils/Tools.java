package com.hapgpt.common.eventbus.core.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: kqyu
 * @Date: 2023/5/6 15:42
 * @Description:
 */
public final class Tools {
    public static String unwrapThrowable(Throwable e) {
        StringBuilder sb = new StringBuilder();
        unwrapThrowable(e, sb);
        return sb.toString();
    }

    public static void unwrapThrowable(Throwable e, StringBuilder sb) {
        sb.append(String.format("\n%s", e));
        //最大打印4k的异常
        if (sb.length() > 1024) {
            return;
        }
        StackTraceElement[] trace = e.getStackTrace();
        List<StackTraceElement> traceElements = Arrays.stream(trace)
                .filter(st->{
                    String className = st.getClassName();
                    //排除代理
                    return  !className.contains("sun.reflect")
                            && !className.contains("java.lang.reflect")
                            && !className.contains("org.springframework.cglib")
                            && !className.contains("org.springframework.aop.framework.CglibAopProxy")
                            && !className.contains("org.springframework.aop.framework.JdkDynamicAopProxy");
                })
                .collect(Collectors.toList());
        for (StackTraceElement traceElement : traceElements) {
            sb.append("\n>\tat " + traceElement);
        }
        Throwable ourCause = e.getCause();
        if (ourCause != null) {
            unwrapThrowable(ourCause, sb);
        }
    }

}
