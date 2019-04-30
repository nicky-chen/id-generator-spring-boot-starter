/*
 * File Name:UUIDGenerator is created on 2019/3/22下午4:05 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.id.send;

import java.util.UUID;

import com.nicky.annotation.IdStrategy;
import com.nicky.constants.IdGeneratorType;
import com.nicky.state.State;
import com.vip.vjtools.vjkit.id.IdUtil;

/**
 * @author nicky_chin
 * @description: uuid算法
 * @date: 2019/3/22 下午4:05
 * @since JDK 1.8
 */
@IdStrategy(enumType = IdGeneratorType.class, enumName = "UUID")
public class UUIDGenerator extends AbstractIdGenerator<String> {

    private static final UUID UUID = IdUtil.fastUUID();

    @Override
    protected State getIdServiceStatus() {
        return getState();
    }

    @Override
    public String getIdService() {
        return UUID.toString();
    }
}
