package com.ibingbo.workflowapp.dto;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangbingbing
 * @date 2020/7/13
 */
public enum OperationTypeEnum {
    ONLINE(0),
    OFFLINE(1),
    REPAIR(2),
    MOVE(3),
    ;
    private Integer type;
    private static Map<Integer, OperationTypeEnum> map = new HashMap<>();

    static {
        EnumSet<OperationTypeEnum> set = EnumSet.allOf(OperationTypeEnum.class);
        set.forEach(operationTypeEnum -> {
            map.put(operationTypeEnum.type, operationTypeEnum);
        });
    }

    OperationTypeEnum(Integer type) {
        this.type = type;
    }

    public static OperationTypeEnum typeOf(Integer type) {
        return map.get(type);
    }

    public Integer getType() {
        return this.type;
    }
}
