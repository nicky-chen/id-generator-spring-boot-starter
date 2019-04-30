/*
 * File Name:EventRouter is created on 2018/12/13下午5:29 by nicky_chen
 *
 * Copyright (c) 2018, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.id.router;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.nicky.constants.ActionEnum;
import com.nicky.util.SpringContextUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author nicky_chin
 * @Description:
 * @date: 2018/12/13 下午5:29
 * @since JDK 1.8
 */
@Getter
@Setter
@NoArgsConstructor
public class EventRouter {

    private static final ConcurrentMap<ActionEnum, AbstractEventHandler> CACHE = new ConcurrentHashMap<>(100);

    private boolean async;

    private boolean available;

    private ActionEnum actionType;

    private AbstractEventHandler handler;

    private EventRouter(RuleBuilder builder) {
        this.async = builder.async;
        this.available = builder.available;
        this.handler = builder.handler;
        this.actionType = builder.actionType;
        EventBus eventBus = (EventBus)SpringContextUtil.getBean("syncBus");
        AsyncEventBus asyncEventBus = (AsyncEventBus)SpringContextUtil.getBean("asyncBus");
        Preconditions.checkNotNull(handler);
        if (CACHE.putIfAbsent(handler.getActionType(), handler) == null) {
            eventBus.register(handler);
            asyncEventBus.register(handler);
        }
    }

    public static RuleBuilder start() {
        return new RuleBuilder();
    }

    public static class RuleBuilder {

        private boolean async;

        private boolean available;

        private ActionEnum actionType;

        private AbstractEventHandler handler;

        public RuleBuilder async(boolean async) {
            this.async = async;
            return this;
        }

        public RuleBuilder available(boolean available) {
            this.available = available;
            return this;
        }

        public RuleBuilder actionType(ActionEnum actionType) {
            this.actionType = actionType;
            return this;
        }

        public RuleBuilder handler(Class<? extends AbstractEventHandler> handler) {
            AbstractEventHandler bean = SpringContextUtil.getBean(handler);
            if (this.handler == null) {
                this.handler = bean;
                return this;
            }
            this.handler.setNextHandler(bean);
            return this;
        }

        public EventRouter end() {
            return new EventRouter(this);

        }

    }

    public <E extends AbstractEvent> void publishEvent(E event) {

        EventBus eventBus = (EventBus)SpringContextUtil.getBean("syncBus");
        AsyncEventBus asyncEventBus = (AsyncEventBus)SpringContextUtil.getBean("asyncBus");
        if (getActionType() == null) {
            return;
        }
        if (!this.available) {
            return;
        }
        if (this.async) {
            asyncEventBus.post(event);
        } else {
            eventBus.post(event);
        }

    }

}
