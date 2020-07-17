package com.ibingbo.workflowapp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author zhangbingbing
 * @date 2020/7/13
 */
@Mapper
public interface OperationMapper {

    @Select("select * from network_operation")
    List getOperations();
}
