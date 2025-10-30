package com.money.manager.money_manager_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/env")
public class EnvController {

    @GetMapping
    public String getEnvVariables() {
        return "POSTGRES_URL: " + System.getenv("POSTGRES_URL") + "\n" +
                "POSTGRES_USER: " + System.getenv("POSTGRES_USER") + "\n" +
                "POSTGRES_PASSWORD: " + System.getenv("POSTGRES_PASSWORD");
    }
}
