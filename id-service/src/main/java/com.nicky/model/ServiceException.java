/*
 * File Name:ServiceException is created on 2019/3/22下午7:11 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 * 
 */
package com.nicky.model;

/**
 * @description: 
 *
 * @author nicky_chin
 * @date: 2019/3/22 下午7:11
 *
 * @since JDK 1.8
 */
public class ServiceException extends RuntimeException {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    protected ServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public static void main(String[] args) {
        String string = new StudentS().toString();
        System.out.println(string);
    }
}
