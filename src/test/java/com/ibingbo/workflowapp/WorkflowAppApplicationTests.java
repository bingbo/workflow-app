package com.ibingbo.workflowapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.ibingbo.workflowapp.dto.Request;
import com.ibingbo.workflowapp.dto.Response;
import com.ibingbo.workflowapp.filter.one.OneFilterChain;

@SpringBootTest
class WorkflowAppApplicationTests {

    @Test
    void contextLoads() {

        OneFilterChain oneFilterChain = new OneFilterChain();
        oneFilterChain.doFilter(new Request(), new Response());
        System.out.println("hello,bill");
    }

}
