package com.example.infrahealthcheck.repository;

import com.example.infrahealthcheck.model.HealthCheckStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisHealthRepository {
    private final StringRedisTemplate redisTemplate;

    public HealthCheckStatus check() {
        long start = System.nanoTime();
        try (RedisConnection connection = redisTemplate.getConnectionFactory().getConnection()) {
            String pong = connection.ping();
            boolean ok = pong != null && !pong.isBlank();
            return HealthCheckStatus.builder()
                .status(ok ? "UP" : "DOWN")
                .details(ok ? "Redis PONG" : "Redis ping failed")
                .responseTimeMs(elapsedMs(start))
                .build();
        } catch (Exception ex) {
            return HealthCheckStatus.builder()
                .status("DOWN")
                .details("Redis error: " + safeMessage(ex))
                .responseTimeMs(elapsedMs(start))
                .build();
        }
    }

    private long elapsedMs(long start) {
        return (System.nanoTime() - start) / 1_000_000;
    }

    private String safeMessage(Exception ex) {
        String message = ex.getMessage();
        return message == null ? ex.getClass().getSimpleName() : message;
    }
}
