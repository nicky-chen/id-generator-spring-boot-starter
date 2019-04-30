package com.nicky.state;

import java.util.concurrent.atomic.AtomicReference;

import lombok.Getter;

/**
 * @author nicky_chin [shuilianpiying@163.com]
 * @since --created on 2018/7/25 at 15:35
 */
@Getter
public class Context extends AbstractState {

    private AtomicReference<State> state;

    public void getIdEvent() {

    }

    public void waitEvent() {
    }

    public void handlerErrorEvent() {

    }
    public void failEvent() {

    }

    @Override
    public String getCurrentState() {
        return state.get().getCurrentState();
    }

}
