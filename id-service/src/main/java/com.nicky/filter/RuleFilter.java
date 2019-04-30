/*
 * File Name:RuleFilter is created on 2019/4/4下午5:41 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.filter;

import java.util.List;

/**
 * @author nicky_chin
 * @description: 规则过滤器
 * @date: 2019/4/4 下午5:41
 * @since JDK 1.8
 */
public interface RuleFilter<T> {

    /**
     * 过滤数据
     *
     * @param ruleList
     *
     * @return
     */
    List<T> filterData(List<T> ruleList);
}
