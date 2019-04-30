/*
 * File Name:StudentS is created on 2019/4/28下午2:15 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.model;

import java.lang.reflect.Method;

import com.nicky.annotation.ToStringBuilder;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import lombok.NoArgsConstructor;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/4/28 下午2:15
 * @since JDK 1.8
 */
@NoArgsConstructor
@ToStringBuilder
public class StudentS {

    private int age;

    private String name;

    @Override
    public String toString() {
        return "cc";
    }

    public void say() {
        System.out.println("sdfsdfsdf");
    }

    public static void main(String[] args) {
        StudentS studentS = new StudentS();
        studentS.age = 1;
        studentS.say();
    }


}
