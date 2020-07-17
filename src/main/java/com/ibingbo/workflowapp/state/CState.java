package com.ibingbo.workflowapp.state;

/**
 * @author zhangbingbing
 * @date 2020/7/11
 */
public class CState implements State {

    @Override
    public void handle(Context context) {
        System.out.println("c state handle");
    }
}
