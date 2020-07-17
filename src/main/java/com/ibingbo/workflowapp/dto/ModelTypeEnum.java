package com.ibingbo.workflowapp.dto;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhangbingbing
 * @date 2020/7/13
 */
public enum ModelTypeEnum {
    ONE(0),
    TWO(1),
    THREE(2),
    ;

    private Integer type;
    private static Map<Integer, ModelTypeEnum> map = new HashMap<>();

    ModelTypeEnum(Integer type) {
        this.type = type;
    }

    static {
        EnumSet<ModelTypeEnum> set = EnumSet.allOf(ModelTypeEnum.class);
        set.forEach(modelTypeEnum -> {
            map.put(modelTypeEnum.type, modelTypeEnum);
        });
    }

    public static ModelTypeEnum typeOf(Integer type) {
        return map.get(type);
    }

    public Integer getType() {
        return this.type;
    }
}
