package com.ibingbo.workflowapp.filter;

import com.ibingbo.workflowapp.dto.ModelTypeEnum;
import com.ibingbo.workflowapp.dto.OperationTypeEnum;
import com.ibingbo.workflowapp.dto.Request;
import com.ibingbo.workflowapp.dto.Response;

/**
 * @author zhangbingbing
 * @date 2020/7/11
 */
public interface Filter {

    void doFilter(Request request, Response response, FilterChain chain);

    Boolean supportsModelType(ModelTypeEnum typeEnum);

    Boolean supportsOperationType(OperationTypeEnum typeEnum);

    Integer getStep();

}
