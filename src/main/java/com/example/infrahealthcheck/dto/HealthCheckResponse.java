package com.example.infrahealthcheck.dto;

public record HealthCheckResponse(
        DependencyStatus mysql,
        DependencyStatus redis,
        DependencyStatus kafka
) {
}
