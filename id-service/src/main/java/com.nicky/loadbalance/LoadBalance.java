/*
 * File Name:LoadBalance is created on 2019/4/12下午6:54 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.loadbalance;

import java.util.List;

/**
 * @author nicky_chin
 * @description: 负载均衡类
 * @date: 2019/4/12 下午6:54
 * @since JDK 1.8
 */
public interface LoadBalance {

    /**
     * Select t.
     *
     * @param <T>      the type parameter
     * @param invokers the invokers
     *
     * @return the t
     *
     * @throws Exception the exception
     */
    <T> T select(List<T> invokers) throws Exception;
}
