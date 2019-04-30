/*
 * File Name:CompositePolicy is created on 2019/3/26上午10:07 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 * 
 */
package com.nicky.id.factory.composite;

/**
 * @description: 
 *
 * @author nicky_chin
 * @date: 2019/3/26 上午10:07
 *
 * @since JDK 1.8
 */
public interface CompositePolicy {

    boolean canRetry();

    void close();


    void registerThrowable(Throwable throwable);
}
