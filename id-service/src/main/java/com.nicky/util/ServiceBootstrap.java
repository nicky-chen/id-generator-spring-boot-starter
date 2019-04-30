/*
 * File Name:ServiceBootstrap is created on 2019/3/22下午7:08 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.util;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @author nicky_chin
 * @description:
 * @date: 2019/3/22 下午7:08
 * @since JDK 1.8
 */
public class ServiceBootstrap {
    public static <S> S loadFirst(Class<S> clazz) {
        Iterator<S> iterator = loadAll(clazz);
        if (!iterator.hasNext()) {
            throw new IllegalStateException(String.format(
                "No implementation defined in /META-INF/services/%s, please check whether the file exists and has the right implementation class!",
                clazz.getName()));
        }
        return iterator.next();
    }

    public static <S> Iterator<S> loadAll(Class<S> clazz) {
        ServiceLoader<S> loader = ServiceLoader.load(clazz);

        return loader.iterator();
    }
}
