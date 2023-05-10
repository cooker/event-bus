package com.hapgpt.common.eventbus.rabbitmq.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @Author: kqyu
 * @Date: 2023/5/10 14:02
 * @Description:
 * @see org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer
 */
public class MQUtils {



    public static String getStackTraceAsString(Throwable cause) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter, true);
        cause.printStackTrace(printWriter);
        return stringWriter.getBuffer().toString();
    }
}
