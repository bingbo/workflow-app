package com.ibingbo.workflowapp.dto;

import java.io.Serializable;

/**
 * @author zhangbingbing
 * @date 2020/7/13
 */
public class AddOperationRequest implements Serializable {

    private Integer modelType;
    private Integer operationType;

    public Integer getModelType() {
        return modelType;
    }

    public void setModelType(Integer modelType) {
        this.modelType = modelType;
    }

    public Integer getOperationType() {
        return operationType;
    }

    public void setOperationType(Integer operationType) {
        this.operationType = operationType;
    }
}
