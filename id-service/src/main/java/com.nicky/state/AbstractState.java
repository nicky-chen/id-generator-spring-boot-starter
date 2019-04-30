/*
 * File Name:AbstractState is created on 2019/4/5下午6:36 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.state;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/4/5 下午6:36
 * @since JDK 1.8
 */
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractState implements State {

    protected static final RuntimeException EXCEPTION = new RuntimeException("操作流程不允许");

    @Delegate
    private State state;

}
