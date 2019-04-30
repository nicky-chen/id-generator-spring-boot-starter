/*
 * File Name:CommonCompositePolicy is created on 2019/3/26上午10:08 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.id.factory.composite;

import java.util.Arrays;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/3/26 上午10:08
 * @since JDK 1.8
 */
public class CommonCompositePolicy implements CompositePolicy {

    CompositePolicy[] policies = new CompositePolicy[0];
    private boolean optimistic = false;

    private ContextSupport context;

    /**
     * Setter for optimistic.
     *
     * @param optimistic should this retry policy be optimistic
     */
    public void setOptimistic(boolean optimistic) {
        this.optimistic = optimistic;
    }

    public void setPolicies(CompositePolicy[] policies) {
        this.policies = Arrays.asList(policies).toArray(new CompositePolicy[policies.length]);
    }

    @Override
    public boolean canRetry() {
        CompositePolicy[] policies = ((CompositeRetryContext)context).policies;

        boolean retryable = true;

        if (this.optimistic) {
            retryable = false;
            for (int i = 0; i < policies.length; i++) {
                if (policies[i].canRetry()) {
                    retryable = true;
                }
            }
        } else {
            for (int i = 0; i < policies.length; i++) {
                if (!policies[i].canRetry()) {
                    retryable = false;
                }
            }
        }

        return retryable;
    }

    @Override
    public void close() {
        CompositePolicy[] policies = ((CompositeRetryContext)context).policies;
        RuntimeException exception = null;
        for (int i = 0; i < policies.length; i++) {
            try {
                policies[i].close();
            } catch (RuntimeException e) {
                if (exception == null) {
                    exception = e;
                }
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    @Override
    public void registerThrowable(Throwable throwable) {
        CompositePolicy[] policies = ((CompositeRetryContext)context).policies;
        for (int i = 0; i < policies.length; i++) {
            policies[i].registerThrowable( throwable);
        }
        context.registerThrowable(throwable);
    }

    private static class CompositeRetryContext extends ContextSupport {

        CompositePolicy[] policies;

    }
}
