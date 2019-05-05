/*
 * File Name:LombokTest is created on 2019/4/30上午9:50 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.model;

import com.nicky.lombok.StringStyle;
import com.nicky.lombok.annotation.ToStringBuilder;

import lombok.Getter;
import lombok.Setter;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/4/30 上午9:50
 * @since JDK 1.8
 */
@ToStringBuilder(toStringStyle = StringStyle.JSON_STYLE)
@Getter
@Setter
public class LombokTest {

    private String name;

    private int age;

    public LombokTest(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        LombokTest lombokTest = new LombokTest("nicky");
        lombokTest.setAge(18);
        System.out.println(lombokTest.toString());
    }
}
