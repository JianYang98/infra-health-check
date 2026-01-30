package com.example.infrahealthcheck.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckStatus {
    private String status;
    private String details;
    private long responseTimeMs;
}