/*
 * File Name:ZookeeperGenerator is created on 2019/3/23下午12:25 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.id.send;

import java.util.Random;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicLong;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.nicky.annotation.IdStrategy;
import com.nicky.constants.IdGeneratorType;
import com.nicky.exception.IdGenerationException;
import com.nicky.state.State;

import lombok.Getter;
import lombok.Setter;

/**
 * @author nicky_chin
 * @description: zookeeper全局id实现 包含无锁方式和加锁方式
 * @date: 2019/3/23 下午12:25
 * @since JDK 1.8
 */
@Getter
@Setter
@IdStrategy(enumType = IdGeneratorType.class, enumName = "ZOOKEEPER")
public class ZookeeperGenerator extends AbstractIdGenerator<Long> {

    private CuratorFramework client;

    private DistributedAtomicLong distAtomicLong;

    private static String ROOT = "/curtor";

    private static String NODE_NAME = "id-generator";

    private static String NAME_PREFIX = "worker-";

    private Random random = new Random(System.currentTimeMillis());

    /**
     * 是否使用分布式锁
     */
    private volatile boolean useDistributedLock;

    public void init() {
        client.getConnectionStateListenable().addListener((conn, state) -> {
            if (state == ConnectionState.RECONNECTED) {
                logger.warn("Try to re-generate my work id.");
                init();
            }
        });

    }

    @Override
    protected State getIdServiceStatus() {
        return getState();
    }

    @Override
    public Long getIdService() {
        if (useDistributedLock) {
            return getIdByDistributedLock();
        }
        return getIdByDistributedAtomicLong();

    }

    private Long getIdByDistributedLock() {
        //请先判断父节点/root节点是否存在
        Stat stat;
        try {
            stat = client.checkExists().forPath(ROOT);
            if (stat == null) {
                client.create().withMode(CreateMode.PERSISTENT).forPath(ROOT, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generateId();

    }

    private Long generateId() {
        long nextTryId = nextTryId();
        if (takeWorkId(nextTryId)) {
            return nextTryId;
        }
        throw new IdGenerationException();
    }

    private long nextTryId() {
        return Math.abs(random.nextLong()) % 1023;
    }

    private boolean takeWorkId(long workerId) {
        try {
            //创建临时节点
            String name =
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(fullPath(workerId));
            return name.endsWith(nodeNameOf(workerId));
        } catch (Exception e) {
            logger.error("Take worker id failed.", e);
        }

        return false;
    }

    private String nodeNameOf(long workerId) {
        return NAME_PREFIX.concat(String.valueOf(workerId));
    }

    private String fullPath(long workerId) {
        return ROOT.concat("/").concat(NODE_NAME);
    }

    /**
     * 原子性自增获取
     *
     * @return
     */
    private Long getIdByDistributedAtomicLong() {
        AtomicValue<Long> sequence;
        try {
            sequence = distAtomicLong.increment();
        } catch (Exception e) {
            logger.error(e::getLocalizedMessage);
            throw new IdGenerationException();
        }
        if (sequence.succeeded()) {
            return sequence.postValue();
        }
        throw new IdGenerationException();
    }
}
