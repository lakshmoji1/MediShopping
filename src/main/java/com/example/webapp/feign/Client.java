package com.example.webapp.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="demo", url="http://localhost:8080/")
public interface Client {
    @GetMapping("/demo")
    String getTheDemo();
}

