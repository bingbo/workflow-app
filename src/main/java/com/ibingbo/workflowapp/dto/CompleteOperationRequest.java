package com.ibingbo.workflowapp.dto;

import java.io.Serializable;

/**
 * @author zhangbingbing
 * @date 2020/7/13
 */
public class CompleteOperationRequest implements Serializable {

    private Long operationId;
    private Integer step;
    private Integer modelType;
    private Integer operationType;

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

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
