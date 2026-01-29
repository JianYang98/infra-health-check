package com.example.infrahealthcheck.controller;

import com.example.infrahealthcheck.dto.HealthCheckResponse;
import com.example.infrahealthcheck.service.HealthCheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping
    public HealthCheckResponse health() {
        return healthCheckService.checkAll();
    }
}
