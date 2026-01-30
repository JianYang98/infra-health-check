package com.example.infrahealthcheck.controller;

import com.example.infrahealthcheck.model.HealthCheckResponse;
import com.example.infrahealthcheck.service.HealthCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class HealthCheckController {
    private final HealthCheckService healthCheckService;

    @GetMapping("/health")
    public ResponseEntity<HealthCheckResponse> health() {
        HealthCheckResponse response = healthCheckService.checkAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/health")
    public ResponseEntity<Void> apiHealth() {
        healthCheckService.checkAll();
        return ResponseEntity.ok().build();
    }
}
