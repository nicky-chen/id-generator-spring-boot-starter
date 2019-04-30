/*
 * File Name:SnowFlakeGenerator is created on 2019/3/23下午12:27 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.id.send;

import com.nicky.spi.IdSender;
import com.nicky.state.NoHavingState;
import com.nicky.state.State;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/3/23 下午12:27
 * @since JDK 1.8
 */
abstract class SnowFlakeGenerator<T> extends AbstractIdGenerator<T> implements IdSender<T> {

    @Override
    protected State getIdServiceStatus() {
        return new NoHavingState();
    }
}
