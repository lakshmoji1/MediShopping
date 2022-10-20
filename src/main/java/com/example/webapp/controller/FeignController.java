package com.example.webapp.controller;

import com.example.webapp.feign.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
public class FeignController {

    @Autowired
    private Client feignClient;

    @GetMapping("/tutorial")
    public String hi() {
        log.info("Fetching the details");
        return feignClient.getTheDemo();
    }
}
