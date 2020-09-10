package com.ibingbo.workflowapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibingbo.workflowapp.dto.AddOperationRequest;
import com.ibingbo.workflowapp.dto.CompleteOperationRequest;
import com.ibingbo.workflowapp.dto.ModelTypeEnum;
import com.ibingbo.workflowapp.dto.OperationTypeEnum;
import com.ibingbo.workflowapp.dto.Request;
import com.ibingbo.workflowapp.dto.Response;
import com.ibingbo.workflowapp.filter.FilterChain;
import com.ibingbo.workflowapp.filter.FilterChainFactory;
import com.ibingbo.workflowapp.filter.FilterManager;
import com.ibingbo.workflowapp.filter.one.OneFilterManager;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}, scanBasePackages = {"com.ibingbo.workflowapp.*"})
@RestController
public class WorkflowAppApplication {

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(WorkflowAppApplication.class, args);
    }

    @RequestMapping("/index")
    public String hello() {
        return "index";
    }

    @PostMapping("/operation/add")
    @ResponseBody
    public String addOperation(@RequestBody AddOperationRequest request) {
        ModelTypeEnum modelTypeEnum = ModelTypeEnum.typeOf(request.getModelType());
        OperationTypeEnum operationTypeEnum = OperationTypeEnum.typeOf(request.getOperationType());
        FilterChain chain = FilterChainFactory.createFilterChain(modelTypeEnum, operationTypeEnum, context);
        chain.doFilter(new Request(), new Response());
        return "add done";
    }

    @PostMapping("/operation/complete")
    @ResponseBody
    public String completeOperation(@RequestBody CompleteOperationRequest request) {
        ModelTypeEnum modelTypeEnum = ModelTypeEnum.typeOf(request.getModelType());
        OperationTypeEnum operationTypeEnum = OperationTypeEnum.typeOf(request.getOperationType());
        FilterManager manager = OneFilterManager.stepOf(request.getStep());
        FilterChain chain = FilterChainFactory.createFilterChain(modelTypeEnum, operationTypeEnum, context);
        chain.doFilter(new Request(), new Response(), manager);
        return "complete done";
    }

}
