package com.ibingbo.workflowapp.state;

/**
 * @author zhangbingbing
 * @date 2020/7/11
 */
public class Context {

    private State state;

    public Context(State state) {
        this.state = state;
    }

    public void request() {
        this.state.handle(this);
    }

    public void next() {
        this.request();
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
