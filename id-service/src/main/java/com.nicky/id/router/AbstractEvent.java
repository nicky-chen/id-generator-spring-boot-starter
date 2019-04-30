/*
 * File Name:AbstractEvent is created on 2018/12/14上午10:55 by nicky_chen
 *
 * Copyright (c) 2018, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.id.router;

import org.springframework.context.ApplicationEvent;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.google.common.eventbus.Subscribe;
import com.nicky.constants.ActionEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * @author nicky_chin
 * @Description:
 * @date: 2018/12/14 上午10:55
 * @since JDK 1.8
 */
@Getter
@Setter
public abstract class AbstractEvent<T, R> extends ApplicationEvent {

    protected ActionEnum actionEnum;

    protected ListenableFutureCallback callback;

    public AbstractEvent(Object source) {
        super(source);
    }

    protected abstract R listener(T t);

    /**
     * 处理接口回调
     *
     * @return
     */
    @Subscribe
    public R handlerListenerWithCallback(T t) {
        if (callback != null) {
            try {
                R r = listener(t);
                callback.onSuccess(r);
                return r;
            } catch (Exception | Error e) {
                callback.onFailure(e);
            }
            return null;
        }
        return listener(t);

    }
}
