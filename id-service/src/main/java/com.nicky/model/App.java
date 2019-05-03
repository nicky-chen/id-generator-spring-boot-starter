/*
 * File Name:App is created on 2019/4/30上午9:50 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.model;

import com.google.common.base.CaseFormat;
import com.nicky.lombok.annotation.ToStringBuilder;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/4/30 上午9:50
 * @since JDK 1.8
 */
@ToStringBuilder
public class App {

    private String value;

    private String value2;

    private int age;

    public App(String value) {
        this.value = value;
    }

    public static void main(String[] args) {
        App app = new App("it works");
        app.age = 11;
        System.out.println(app.toString());
        //System.out.println(app.getValue());
    }
}
