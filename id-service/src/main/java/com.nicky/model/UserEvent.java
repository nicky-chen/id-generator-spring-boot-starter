/*
 * File Name:UserEvent is created on 2018/12/13下午2:12 by nicky_chen
 *
 * Copyright (c) 2018, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.model;

import com.nicky.id.router.AbstractEvent;
import com.nicky.lombok.adapter.AdapterFactory;
import com.vip.vjtools.vjkit.base.annotation.NotNull;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import lombok.Getter;
import lombok.Setter;

/**
 * @author nicky_chin
 * @Description:
 * @date: 2018/12/13 下午2:12
 * @since JDK 1.8
 */
@Getter
@Setter
public class UserEvent extends AbstractEvent<String, String> {



    private String user;

    public UserEvent(@NotNull String user) {
        super(user);
        this.user = user;
    }

    @Override
    protected String listener(String string) {
        return "null";
    }

    @Override
    public String toString() {
        return AdapterFactory.builderStringAdapter(1 << 2);
    }
}
