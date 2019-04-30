/*
 * File Name:AbstractEventHandler is created on 2018/12/14上午11:10 by nicky_chen
 *
 * Copyright (c) 2018, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.id.router;

import java.util.Optional;

import org.springframework.context.ApplicationListener;

import com.nicky.constants.ActionEnum;

/**
 * @author nicky_chin
 * @Description:
 * @date: 2018/12/14 上午11:10
 * @since JDK 1.8
 */
public abstract class AbstractEventHandler<E extends AbstractEvent> implements ApplicationListener<E> {

    /**
     * 下一个责任链成员
     */
    protected AbstractEventHandler<E> nextHandler;

    public boolean checkPredicate(E e) {
        return e.getActionEnum() != getActionType();
    }

    public abstract ActionEnum getActionType();

    public AbstractEventHandler<E> getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(AbstractEventHandler<E> nextHandler) {
        this.nextHandler = nextHandler;
    }

    void handleNextEvent(E e) {
        Optional.ofNullable(getNextHandler()).ifPresent(x -> x.onApplicationEvent(e));
    }

}
