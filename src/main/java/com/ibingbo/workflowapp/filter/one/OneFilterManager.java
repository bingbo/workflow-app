package com.ibingbo.workflowapp.filter.one;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.ibingbo.workflowapp.filter.FilterManager;

/**
 * @author zhangbingbing
 * @date 2020/7/11
 */
public enum OneFilterManager implements FilterManager {
    A_FILTER(1),
    B_FILTER(2),
    C_FILTER(3),
    ;

    private Integer step;

    OneFilterManager(Integer step) {
        this.step = step;
    }

    private static Map<Integer, OneFilterManager> map = new HashMap<>();

    static {
        EnumSet<OneFilterManager> set = EnumSet.allOf(OneFilterManager.class);
        set.forEach(operationTypeEnum -> {
            map.put(operationTypeEnum.step, operationTypeEnum);
        });
    }

    @Override
    public Integer getStep() {
        return this.step;
    }

    public static OneFilterManager stepOf(Integer step) {
        return map.get(step);
    }
}
