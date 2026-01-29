package com.example.infrahealthcheck.service;

import com.example.infrahealthcheck.config.HealthCheckProperties;
import com.example.infrahealthcheck.dto.DependencyStatus;
import com.example.infrahealthcheck.dto.HealthCheckResponse;
import com.example.infrahealthcheck.repository.DatabaseHealthCheckRepository;
import com.example.infrahealthcheck.repository.KafkaHealthCheckRepository;
import com.example.infrahealthcheck.repository.RedisHealthCheckRepository;
import org.springframework.stereotype.Service;

@Service
public class HealthCheckService {

    private final DatabaseHealthCheckRepository databaseRepository;
    private final RedisHealthCheckRepository redisRepository;
    private final KafkaHealthCheckRepository kafkaRepository;
    private final HealthCheckProperties properties;

    public HealthCheckService(
            DatabaseHealthCheckRepository databaseRepository,
            RedisHealthCheckRepository redisRepository,
            KafkaHealthCheckRepository kafkaRepository,
            HealthCheckProperties properties
    ) {
        this.databaseRepository = databaseRepository;
        this.redisRepository = redisRepository;
        this.kafkaRepository = kafkaRepository;
        this.properties = properties;
    }

    public HealthCheckResponse checkAll() {
        DependencyStatus mysql = checkMysql();
        DependencyStatus redis = checkRedis();
        DependencyStatus kafka = checkKafka();
        return new HealthCheckResponse(mysql, redis, kafka);
    }

    private DependencyStatus checkMysql() {
        long start = System.nanoTime();
        try {
            Integer result = databaseRepository.check();
            long durationMs = toMillis(start);
            return new DependencyStatus("UP", "SELECT 1 -> " + result, durationMs);
        } catch (Exception ex) {
            long durationMs = toMillis(start);
            return new DependencyStatus("DOWN", ex.getMessage(), durationMs);
        }
    }

    private DependencyStatus checkRedis() {
        long start = System.nanoTime();
        try {
            String pong = redisRepository.ping();
            long durationMs = toMillis(start);
            return new DependencyStatus("UP", "PING -> " + pong, durationMs);
        } catch (Exception ex) {
            long durationMs = toMillis(start);
            return new DependencyStatus("DOWN", ex.getMessage(), durationMs);
        }
    }

    private DependencyStatus checkKafka() {
        long start = System.nanoTime();
        try {
            kafkaRepository.validateTemplate();
            HealthCheckProperties.Kafka kafka = properties.getKafka();
            if (kafka.isSendTestMessage()) {
                kafkaRepository.sendTestMessage(kafka.getTestTopic(), kafka.getTimeoutMs());
            }
            long durationMs = toMillis(start);
            String message = kafka.isSendTestMessage()
                    ? "Producer ok, message sent"
                    : "Producer ok";
            return new DependencyStatus("UP", message, durationMs);
        } catch (Exception ex) {
            long durationMs = toMillis(start);
            return new DependencyStatus("DOWN", ex.getMessage(), durationMs);
        }
    }

    private long toMillis(long start) {
        return (System.nanoTime() - start) / 1_000_000;
    }
}
