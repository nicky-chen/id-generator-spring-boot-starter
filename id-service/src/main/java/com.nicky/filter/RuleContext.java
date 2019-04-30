/*
 * File Name:RuleContext is created on 2019/4/4下午5:55 by nicky_chen
 *
 * Copyright (c) 2019, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.filter;

import java.util.List;

import com.google.common.collect.Lists;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import jodd.typeconverter.TypeConverterManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

/**
 * @author nicky_chin
 * @description:过滤执行器
 * @date: 2019/4/4 下午5:55
 * @since JDK 1.8
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RuleContext {

    private List rules;

    private List<RuleFilter> filterList;

    private RuleContext(RuleBuilder builder) {
        this.filterList = builder.filterList;
    }

    /**
     * 规则建造器
     * @param rules
     * @return
     */
    public static RuleBuilder rules(List rules) {
        return new RuleBuilder().addRules(rules);
    }

    public static class RuleBuilder {

        @Setter(value = AccessLevel.PRIVATE)
        private List rules;

        private List<RuleFilter> filterList;

        public RuleBuilder addFilter(RuleFilter filter) {
            filterList.add(filter);
            return this;
        }

        private RuleBuilder addRules(List rules) {
            this.setRules(rules);
            this.filterList = Lists.newArrayList();
            return this;
        }

        public <C extends List<T>, T> C invokerFilter(@NonNull Class<T> returnType) {
            RuleContext ruleContext = new RuleContext(this);
            ruleContext.setRules(rules);
            ruleContext.setFilterList(filterList);
            ruleContext.getFilterList().forEach(filter -> {
                List rules = ruleContext.getRules();
                if (CollectionUtil.isNotEmpty(rules)) {
                    ruleContext.setRules(filter.filterData(rules));
                }
            });
            return TypeConverterManager.get().convertToCollection(ruleContext.getRules(), List.class, returnType);
        }
    }

}
