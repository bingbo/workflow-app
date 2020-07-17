package com.ibingbo.workflowapp.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;

import com.ibingbo.workflowapp.dto.ModelTypeEnum;
import com.ibingbo.workflowapp.dto.OperationTypeEnum;

/**
 * @author zhangbingbing
 * @date 2020/7/13
 */
public class FilterChainFactory {

    private static Map<String, FilterChain> filterChainMap = new ConcurrentHashMap<>();

    private FilterChainFactory() {

    }

    public static FilterChain createFilterChain(ModelTypeEnum modelType, OperationTypeEnum operationType,
                                                ApplicationContext context) {
        String key = String.format("%d-%d", modelType.getType(), operationType.getType());
        if (filterChainMap.containsKey(key)) {
            return filterChainMap.get(key);
        }
        FilterChain filterChain = null;
        Map<String, FilterChain> chainMap = context.getBeansOfType(FilterChain.class);
        if (chainMap != null && !chainMap.isEmpty()) {
            for (FilterChain chain : chainMap.values()) {
                if (chain.supportsModelType(modelType) && chain.supportsOperationType(operationType)) {
                    filterChain = chain;
                    break;
                }
            }
        }
        Map<String, Filter> filterMap = context.getBeansOfType(Filter.class);
        List<Filter> filters = new ArrayList<>();
        if (filterMap != null && !filterMap.isEmpty()) {
            for (Filter filter : filterMap.values()) {
                if (filter.supportsModelType(modelType) && filter.supportsOperationType(operationType)) {
                    filters.add(filter);
                }
            }
        }
        if (filterChain != null && !filters.isEmpty()) {
            Collections.sort(filters, new Comparator<Filter>() {
                @Override
                public int compare(Filter o1, Filter o2) {
                    return o1.getStep() - o2.getStep();
                }
            });
            filterChain.addFilters(filters);
            filterChainMap.put(key, filterChain);
            return filterChain;
        }
        return null;
    }

}
