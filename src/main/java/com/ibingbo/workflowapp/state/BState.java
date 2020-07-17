package com.ibingbo.workflowapp.state;

/**
 * @author zhangbingbing
 * @date 2020/7/11
 */
public class BState implements State {

    @Override
    public void handle(Context context) {
        System.out.println("b state handle");
        context.setState(new CState());
        context.request();
    }
}
