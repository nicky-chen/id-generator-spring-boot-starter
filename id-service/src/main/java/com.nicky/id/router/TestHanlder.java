/*
 * File Name:TestHanlder is created on 2018/12/14上午10:15 by nicky_chen
 *
 * Copyright (c) 2018, xiaoyujiaoyu technology All Rights Reserved.
 *
 */
package com.nicky.id.router;

import org.springframework.stereotype.Component;

import com.google.common.eventbus.Subscribe;
import com.nicky.constants.ActionEnum;
import com.nicky.model.UserEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * @author nicky_chin
 * @Description:
 * @date: 2018/12/14 上午10:15
 * @since JDK 1.8
 */
@Slf4j
@Component
public class TestHanlder extends AbstractEventHandler<UserEvent> {

    @Subscribe
    @Override
    public void onApplicationEvent(UserEvent event) {
        if (checkPredicate(event)) {
            return;
        }
        log.info("event:{}", event);
        handleNextEvent(event);
    }

    @Override
    public ActionEnum getActionType() {
        return ActionEnum.ONE;
    }
}
