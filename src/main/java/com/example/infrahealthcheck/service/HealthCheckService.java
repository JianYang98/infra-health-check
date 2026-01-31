package com.example.infrahealthcheck.service;

import com.example.infrahealthcheck.model.HealthCheckResponse;
import com.example.infrahealthcheck.model.HealthCheckStatus;
import com.example.infrahealthcheck.repository.DbHealthRepository;
import com.example.infrahealthcheck.repository.KafkaHealthRepository;
import com.example.infrahealthcheck.repository.RedisHealthRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthCheckService {
    private final DbHealthRepository dbHealthRepository;
    private final RedisHealthRepository redisHealthRepository;
    private final KafkaHealthRepository kafkaHealthRepository;

    public HealthCheckResponse checkAll() {
        HealthCheckStatus mysql = dbHealthRepository.check();
        HealthCheckStatus redis = redisHealthRepository.check();
        HealthCheckStatus kafka = kafkaHealthRepository.check();

        String overall = ("UP".equals(mysql.getStatus())
            && "UP".equals(redis.getStatus())
            && "UP".equals(kafka.getStatus())) ? "UP" : "DOWN";

        return HealthCheckResponse.builder()
            .overallStatus(overall)
            .mysql(mysql)
            .redis(redis)
            .kafka(kafka)
            .checkedAt(Instant.now())
            .build();
    }
}
