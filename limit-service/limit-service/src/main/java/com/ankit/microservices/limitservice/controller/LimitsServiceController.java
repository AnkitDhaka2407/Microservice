package com.ankit.microservices.limitservice.controller;

import com.ankit.microservices.limitservice.configuration.Configuration;
import com.ankit.microservices.limitservice.entity.Limits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitsServiceController {


    @Autowired
    private Configuration configuration;
    @GetMapping("/limits")
    public Limits retrieveLimits(){
        return new Limits(configuration.getMinimum(),configuration.getMaximum());
    }

}
