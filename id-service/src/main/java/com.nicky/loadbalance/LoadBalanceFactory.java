/*
 * File Name:LoadBalanceFactory is created on 2019/4/12下午6:54 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.loadbalance;

import com.nicky.util.EnhancedServiceLoader;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/4/12 下午6:54
 * @since JDK 1.8
 */
public class LoadBalanceFactory {

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static LoadBalance getInstance() {
        return EnhancedServiceLoader.load(LoadBalance.class);
    }
}
