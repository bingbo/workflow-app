package com.ibingbo.workflowapp.state;

/**
 * @author zhangbingbing
 * @date 2020/7/11
 */
public class AState implements State {

    @Override
    public void handle(Context context) {
        System.out.println("a state handle");
        context.setState(new BState());
        context.request();
    }
}
