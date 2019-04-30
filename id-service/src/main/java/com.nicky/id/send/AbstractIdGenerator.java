/*
 * File Name:AbstractIdGenerator is created on 2019/3/22下午4:01 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.id.send;

import java.util.concurrent.TimeUnit;

import com.nicky.spi.IdSender;
import com.nicky.state.State;

import jodd.log.Logger;
import jodd.log.LoggerFactory;
import lombok.Getter;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/3/22 下午4:01
 * @since JDK 1.8
 */
@Getter
public abstract class AbstractIdGenerator<T> implements IdSender<T> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected State state;

    /**
     * 获取服务状态
     *
     * @return
     */
    protected abstract State getIdServiceStatus();

    protected T getIdServiceWithTimeOut(TimeUnit unit, long ms) {
        return null;
    }

}
