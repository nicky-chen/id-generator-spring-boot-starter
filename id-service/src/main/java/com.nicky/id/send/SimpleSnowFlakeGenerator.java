/*
 * File Name:SimpleSnowFlakeGenerator is created on 2019/3/26上午9:51 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.id.send;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.StampedLock;

import com.google.common.base.Preconditions;
import com.nicky.annotation.IdStrategy;
import com.nicky.constants.IdGeneratorType;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author nicky_chin
 * @description: 通用雪花算法
 * @date: 2019/3/26 上午9:51
 * @since JDK 1.8
 */
@Getter
@Setter
@ToString
@IdStrategy(enumType = IdGeneratorType.class, enumName = "SIMPLE_SNOW_FLAKE")
public class SimpleSnowFlakeGenerator extends SnowFlakeGenerator<Long> {

    //idEpoch + dataCenterId + workerId + sequence  63位

    /**
     * 时间戳 41位
     */
    private final long idEpoch;

    /**
     * 数据中心id 5位
     */
    private final long dataCenterId;

    /**
     * 服务器id 5位
     */
    private final long workerId;

    /**
     * 序列号 12位
     */
    private AtomicLong sequence;

    private long lastTimestamp = -1L;

    private static final ThreadLocalRandom SEED = ThreadLocalRandom.current();

    /**
     * 前5位作为数据中心机房标识，后5位作为同一机房机器标识 总共10位
     */
    private static final long WORKER_ID_BITS = 5L;

    private static final long DATA_CENTER_ID_BITS = 5L;

    private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);

    /**
     * 单机房最多机器为    1111 1111 ^ 1110 0000   取反 0001 1111
     */
    private static final long MAX_DATA_CENTER_ID = ~(-1L << DATA_CENTER_ID_BITS);

    /**
     * 序列号12位
     */
    private static final long SEQUENCE_BITS = 12L;

    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATA_CENTER_ID_BITS;
    private static final long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    private static final StampedLock LOCK = new StampedLock();

    /**
     * 时钟回拨容忍度
     */
    private static final int MAX_BACKWARD_MS = 5;

    /**
     * 默认时间戳
     */
    private static final long DEFAULT_EPOCH = 1200000000000L;

    public SimpleSnowFlakeGenerator() {
        this(DEFAULT_EPOCH);
    }

    public SimpleSnowFlakeGenerator(long idEpoch) {
        this(SEED.nextInt((int)MAX_WORKER_ID), SEED.nextInt((int)MAX_DATA_CENTER_ID), new AtomicLong(0), idEpoch);
    }

    public SimpleSnowFlakeGenerator(long workerId, long dataCenterId, AtomicLong sequence) {
        this(workerId, dataCenterId, sequence, DEFAULT_EPOCH);
    }

    public SimpleSnowFlakeGenerator(long workerId, long dataCenterId, AtomicLong sequence, long idEpoch) {
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        this.sequence = sequence;
        this.idEpoch = idEpoch;
        Preconditions.checkArgument(workerId >= 0 && workerId <= MAX_WORKER_ID,
            String.format("workerId is illegal: %s", workerId));
        Preconditions.checkArgument(dataCenterId >= 0 && dataCenterId <= MAX_DATA_CENTER_ID,
            String.format("dataCenterId is illegal: %s", workerId));
        Preconditions
            .checkArgument(idEpoch < System.currentTimeMillis(), String.format("idEpoch is illegal: %s", idEpoch));
    }

    @Override
    public Long getIdService() {
        //上锁效率低
        long stamp = LOCK.writeLock();
        try {
            return nextId();
        } finally {
            LOCK.unlockWrite(stamp);
        }
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long nextId() {
        long timestamp = timeGen();
        //新时间戳时间必须大于最新一次生成时间
        //但需要考虑服务器时钟回拨问题
        if (timestamp < lastTimestamp) {
            //如果时钟回拨在可接受范围内, 等待即可
            if (lastTimestamp - timestamp < MAX_BACKWARD_MS) {
                try {
                    TimeUnit.MILLISECONDS.sleep(lastTimestamp - timestamp);
                } catch (InterruptedException e) {
                    throw new IllegalStateException("Clock interrupted");
                }
                timestamp = timeGen();
            }
        }
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("Clock moved backwards.");
        }
        //相等则序列号增1
        long init = lastTimestamp & 1;
        if (lastTimestamp == timestamp) {
            sequence.set((sequence.getAndIncrement()) & SEQUENCE_MASK);
            if (sequence.get() == init) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //采用随机值，方式低并发下id都是奇数或者偶数
            sequence.set(init);
        }
        lastTimestamp = timestamp;
        return ((timestamp - idEpoch) << TIMESTAMP_LEFT_SHIFT) | (dataCenterId << DATA_CENTER_ID_SHIFT) | (workerId
            << WORKER_ID_SHIFT) | sequence.get();
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis() & 1);
        SimpleSnowFlakeGenerator worker = new SimpleSnowFlakeGenerator(11111L);
        System.out.println(worker.getIdService());
        System.out.println(worker.toString());

    }

}
