/*
 * File Name:IdGeneratorContext is created on 2019/4/5下午9:58 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky;

import java.util.List;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.nicky.annotation.IdStrategy;
import com.nicky.calculate.IdPersistence;
import com.nicky.id.factory.composite.CommonCompositePolicy;
import com.nicky.spi.IdFailStrategy;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Singular;

/**
 * @author nicky_chin
 * @description: id建造者
 * @date: 2019/4/5 下午9:58
 * @since JDK 1.8
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdGeneratorContext {

    /**
     * 生成策略
     */
    @Singular("genStrategy")
    private List<IdStrategy> idStrategies;

    /**
     * 失败策略
     */
    @Singular("failStrategy")
    private List<IdFailStrategy> idFailStrategies;

    /**
     * 持久化策略
     */
    private IdPersistence persistStrategy;

    /**
     * 重试策略
     */
    private CommonCompositePolicy compositePolicy;

    /**
     * 线程池
     */
    private ListeningExecutorService executorService;

    /**
     * 每秒限流数量
     */
    private Long maxThroughOut;

    /**
     * 限流开关
     */
    private boolean limitingStatus;
}
