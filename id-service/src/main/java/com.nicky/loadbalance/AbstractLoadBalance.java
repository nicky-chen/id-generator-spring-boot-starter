/*
 * File Name:AbstractLoadBalance is created on 2019/4/12下午6:53 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.loadbalance;

import java.util.List;

import org.springframework.util.CollectionUtils;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/4/12 下午6:53
 * @since JDK 1.8
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public <T> T select(List<T> invokers) {
        if (CollectionUtils.isEmpty(invokers)) {
            return null;
        }
        if (invokers.size() == 1) {
            return invokers.get(0);
        }
        return doSelect(invokers);
    }

    /**
     * Do select t.
     *
     * @param <T>      the type parameter
     * @param invokers the invokers
     *
     * @return the t
     */
    protected abstract <T> T doSelect(List<T> invokers);
}
