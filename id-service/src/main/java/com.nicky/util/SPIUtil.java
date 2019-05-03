/*
 * File Name:SPIUtil is created on 2019/3/22下午7:07 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.util;

import com.google.common.base.Throwables;
import com.nicky.model.LoggerEnhancer;
import com.nicky.model.ServiceException;
import com.nicky.spi.Injector;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/3/22 下午7:07
 * @since JDK 1.8
 */
public class SPIUtil {

    private static final LoggerEnhancer LOGGER = LoggerEnhancer.getLogger(SPIUtil.class);

    private static volatile Injector s_injector;
    private static final Object lock = new Object();

    private static Injector getInjector() {
        if (s_injector == null) {
            synchronized (lock) {
                if (s_injector == null) {
                    try {
                        s_injector = ServiceBootstrap.loadFirst(Injector.class);
                    } catch (Throwable ex) {
                        ServiceException exception = new ServiceException("Unable to initialize Apollo Injector!", ex);
                        LOGGER.error(ex::getLocalizedMessage, ex);
                        throw exception;
                    }
                }
            }
        }

        return s_injector;
    }

    public static <T> T getInstance(Class<T> clazz) {
        try {
            return getInjector().getInstance(clazz);
        } catch (Throwable ex) {
            LOGGER.error(() -> Throwables.getStackTraceAsString(ex));
            throw new ServiceException(String.format("Unable to load instance for type %s!", clazz.getName()), ex);
        }
    }

    public static <T> T getInstance(Class<T> clazz, String name) {
        try {
            return getInjector().getInstance(clazz, name);
        } catch (Throwable ex) {
            LOGGER.error(() -> Throwables.getStackTraceAsString(ex));
            throw new ServiceException(
                String.format("Unable to load instance for type %s and name %s !", clazz.getName(), name), ex);
        }
    }
}

