package com.example.infrahealthcheck.repository;

import com.example.infrahealthcheck.model.HealthCheckStatus;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class KafkaHealthRepository {
    private final KafkaAdmin kafkaAdmin;

    public HealthCheckStatus check() {
        long start = System.nanoTime();
        Map<String, Object> config = kafkaAdmin.getConfigurationProperties();
        try (AdminClient adminClient = AdminClient.create(config)) {
            DescribeClusterResult result = adminClient.describeCluster();
            String clusterId = result.clusterId().get(3, TimeUnit.SECONDS);
            boolean ok = clusterId != null && !clusterId.isBlank();
            return HealthCheckStatus.builder()
                .status(ok ? "UP" : "DOWN")
                .details(ok ? "Kafka cluster OK" : "Kafka cluster id empty")
                .responseTimeMs(elapsedMs(start))
                .build();
        } catch (Exception ex) {
            return HealthCheckStatus.builder()
                .status("DOWN")
                .details("Kafka error: " + safeMessage(ex))
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
