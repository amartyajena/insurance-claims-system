package com.insurance.auth.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @GetMapping("/public")
    public String publicApi() {
        return "Public API is working";
    }

    @GetMapping("/customer")
    public String customerApi() {
        return "Customer API is accessible";
    }

    @GetMapping("/agent")
    public String agentApi() {
        return "Agent API is accessible";
    }

    @GetMapping("/admin")
    public String adminApi() {
        return "Admin API is accessible";
    }
}