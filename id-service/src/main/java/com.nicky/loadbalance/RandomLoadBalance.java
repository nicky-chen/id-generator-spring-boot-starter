/*
 * File Name:RandomLoadBalance is created on 2019/4/12下午6:55 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.loadbalance;

import java.util.List;
import java.util.Random;

import com.nicky.annotation.LoadLevel;

/**
 * @author nicky_chin
 * @description: 随机算法
 * @date: 2019/4/12 下午6:55
 * @since JDK 1.8
 */
@LoadLevel(name = "RandomLoadBalance", order = 2)
public class RandomLoadBalance extends AbstractLoadBalance {

    private final Random random = new Random();

    @Override
    protected <T> T doSelect(List<T> invokers) {
        int length = invokers.size();
        return invokers.get(random.nextInt(length));
    }

}
