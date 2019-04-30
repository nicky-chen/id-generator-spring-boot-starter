/*
 * File Name:NoHavingState is created on 2019/3/23下午12:21 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.state;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/3/23 下午12:21
 * @since JDK 1.8
 */
public class NoHavingState implements State {
    @Override
    public void getIdEvent(Context context) {

    }

    @Override
    public void waitEvent(Context context) {

    }

    @Override
    public void handlerErrorEvent(Context context) {

    }

    @Override
    public void failEvent(Context context) {

    }

    @Override
    public String getCurrentState() {
        return null;
    }
}
