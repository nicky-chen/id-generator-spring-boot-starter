/*
 * File Name:ContextSupport is created on 2019/3/26上午10:12 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 * 
 */
package com.nicky.id.factory.composite;

/**
 * @description: 
 *
 * @author nicky_chin
 * @date: 2019/3/26 上午10:12
 *
 * @since JDK 1.8
 */
public class ContextSupport implements CompositePolicy {
    @Override
    public boolean canRetry() {
        return false;
    }

    @Override
    public void close() {

    }

    @Override
    public void registerThrowable(Throwable throwable) {

    }
}
