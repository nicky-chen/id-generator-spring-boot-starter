/*
 * File Name:App is created on 2019/4/30上午9:50 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.model;


/**
 * @author nicky_chin
 * @description:
 * @date: 2019/4/30 上午9:50
 * @since JDK 1.8
 */
public class App {

    private String value;

    private String value2;

    public App(String value) {
        this.value = value;
    }

    public static void main(String[] args) {
        App app = new App("it works");
        //System.out.println(app.getValue());
    }
}
