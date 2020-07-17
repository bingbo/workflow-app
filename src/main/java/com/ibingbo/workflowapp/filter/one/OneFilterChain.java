package com.ibingbo.workflowapp.filter.one;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.ibingbo.workflowapp.dto.ModelTypeEnum;
import com.ibingbo.workflowapp.dto.OperationTypeEnum;
import com.ibingbo.workflowapp.dto.Request;
import com.ibingbo.workflowapp.dto.Response;
import com.ibingbo.workflowapp.filter.Filter;
import com.ibingbo.workflowapp.filter.FilterChain;
import com.ibingbo.workflowapp.filter.FilterManager;

/**
 * @author zhangbingbing
 * @date 2020/7/11
 */
@Component
public class OneFilterChain implements FilterChain {

    private Integer position = 0;
    private Integer total = 0;
    private List<Filter> filters = new ArrayList<>();

    @Override
    public void addFilters(List<Filter> filters) {
        this.filters.addAll(filters);
        this.total = this.filters.size();
    }

    @Override
    public void doFilter(Request req, Response res) {
        if (this.position < this.total) {
            this.filters.get(this.position++).doFilter(req, res, this);
            return;
        }
        this.doService(req, res);
    }

    @Override
    public void doFilter(Request req, Response res, Integer step) {
        this.position = this.position + step;
        this.doFilter(req, res);
    }

    @Override
    public void doFilter(Request req, Response res, FilterManager manager) {
        Integer index = manager.getStep();
        this.position = index - 1;
        this.doFilter(req, res);
    }

    @Override
    public void doService(Request req, Response res) {
        System.out.println("do service");
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
