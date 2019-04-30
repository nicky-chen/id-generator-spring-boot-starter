/*
 * File Name:RedisGenerator is created on 2019/3/23下午12:26 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 * 
 */
package com.nicky.id.send;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.redisson.api.RLongAdder;
import org.redisson.api.RedissonClient;

import com.nicky.annotation.IdStrategy;
import com.nicky.constants.IdGeneratorType;
import com.nicky.state.State;

/**
 * @description: redis自增id
 *
 * @author nicky_chin
 * @date: 2019/3/23 下午12:26
 *
 * @since JDK 1.8
 */
@IdStrategy(enumType = IdGeneratorType.class, enumName = "REDIS")
public class RedisGenerator extends AbstractIdGenerator<Long> {


    private RedissonClient client;

    /**
     * 是否使用map
     */
    private volatile boolean userMap;

    @Override
    protected State getIdServiceStatus() {
        return null;
    }

    @Override
    public Long getIdService() {
        try {
            return getIdByDistributedLongAdder();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }

    }

    /**
     * 原子性自增获取
     *
     * @return
     */
    private Long getIdByDistributedLongAdder() throws InterruptedException, ExecutionException, TimeoutException {
        RLongAdder idClient = client.getLongAdder("id:generator:adder");
        idClient.increment();
        return idClient.sumAsync().get(3, TimeUnit.SECONDS);
    }

    //todo 原子性脚本
    private void incersss() {
        //client.getScript().eval()
    }
}
