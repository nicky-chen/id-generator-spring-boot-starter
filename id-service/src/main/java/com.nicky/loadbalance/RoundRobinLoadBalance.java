/*
 * File Name:RoundRobinLoadBalance is created on 2019/4/12下午6:56 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author nicky_chin
 * @description: robin算法
 * @date: 2019/4/12 下午6:56
 * @since JDK 1.8
 */
public class RoundRobinLoadBalance extends AbstractLoadBalance {

    private final AtomicInteger sequence = new AtomicInteger();

    @Override
    protected <T> T doSelect(List<T> invokers) {
        int length = invokers.size();
        return invokers.get(getPositiveSequence() % length);
    }

    private int getPositiveSequence() {
        for (; ; ) {
            int current = sequence.get();
            int next = (current >= Integer.MAX_VALUE ? 0 : current + 1);
            if (sequence.compareAndSet(current, next)) {
                return current;
            }
        }
    }

}
