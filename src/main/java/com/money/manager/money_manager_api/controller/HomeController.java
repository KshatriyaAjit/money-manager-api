package com.money.manager.money_manager_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/health")
    public String healthCheck() {
        return "Application is running";
    }
}