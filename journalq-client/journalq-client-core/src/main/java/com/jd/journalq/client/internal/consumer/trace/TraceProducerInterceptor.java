package com.jd.journalq.client.internal.consumer.trace;

import com.jd.journalq.client.internal.common.ordered.Ordered;
import com.jd.journalq.client.internal.producer.domain.SendResult;
import com.jd.journalq.client.internal.producer.interceptor.ProduceContext;
import com.jd.journalq.client.internal.producer.interceptor.ProducerInterceptor;
import com.jd.journalq.client.internal.trace.TraceBuilder;
import com.jd.journalq.client.internal.trace.TraceCaller;
import com.jd.journalq.client.internal.trace.TraceType;

import java.util.List;

/**
 * TraceProducerInterceptor
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2019/1/11
 */
public class TraceProducerInterceptor implements ProducerInterceptor, Ordered {

    private static final String CALLER_KEY = "_TRACE_CALLER_";

    @Override
    public boolean preSend(ProduceContext context) {
        TraceCaller caller = TraceBuilder.newInstance()
                .topic(context.getTopic())
                .app(context.getApp())
                .namespace(context.getNameserver().getNamespace())
                .type(TraceType.PRODUCER_SEND)
                .begin();
        context.putAttribute(CALLER_KEY, caller);
        return true;
    }

    @Override
    public void postSend(ProduceContext context, List<SendResult> result) {
        TraceCaller caller = context.getAttribute(CALLER_KEY);
        if (caller == null) {
            return;
        }
        caller.end();
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}