package com.example.infrahealthcheck.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckResponse {
    private String overallStatus;
    private HealthCheckStatus mysql;
    private HealthCheckStatus redis;
    private HealthCheckStatus kafka;
    private Instant checkedAt;
}
