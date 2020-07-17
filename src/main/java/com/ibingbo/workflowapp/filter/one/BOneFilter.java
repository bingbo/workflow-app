package com.ibingbo.workflowapp.filter.one;

import org.springframework.stereotype.Component;

import com.ibingbo.workflowapp.dto.ModelTypeEnum;
import com.ibingbo.workflowapp.dto.OperationTypeEnum;
import com.ibingbo.workflowapp.dto.Request;
import com.ibingbo.workflowapp.dto.Response;
import com.ibingbo.workflowapp.filter.Filter;
import com.ibingbo.workflowapp.filter.FilterChain;

/**
 * @author zhangbingbing
 * @date 2020/7/11
 */
@Component
public class BOneFilter implements Filter {

    private Integer index;

    @Override
    public void doFilter(Request req, Response res, FilterChain chain) {
        System.out.println("filter b doing");
        chain.doFilter(req, res);
        System.out.println("filter b done");
    }

    @Override
    public Integer getStep() {
        return OneFilterManager.B_FILTER.getStep();
    }

    @Override
    public Boolean supportsModelType(ModelTypeEnum typeEnum) {
        return ModelTypeEnum.ONE == typeEnum;
    }

    @Override
    public Boolean supportsOperationType(OperationTypeEnum typeEnum) {
        return OperationTypeEnum.ONLINE == typeEnum;
    }
}
