package com.ibingbo.workflowapp.filter;

import java.util.List;

import com.ibingbo.workflowapp.dto.ModelTypeEnum;
import com.ibingbo.workflowapp.dto.OperationTypeEnum;
import com.ibingbo.workflowapp.dto.Request;
import com.ibingbo.workflowapp.dto.Response;

/**
 * @author zhangbingbing
 * @date 2020/7/11
 */
public interface FilterChain {

    void addFilters(List<Filter> filters);

    void doFilter(Request request, Response response);

    void doFilter(Request request, Response response, Integer step);

    void doFilter(Request req, Response res, FilterManager manager);

    void doService(Request request, Response response);

    Boolean supportsModelType(ModelTypeEnum typeEnum);

    Boolean supportsOperationType(OperationTypeEnum typeEnum);
}
