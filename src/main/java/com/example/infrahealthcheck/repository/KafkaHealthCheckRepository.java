package com.example.infrahealthcheck.repository;

import java.util.concurrent.TimeUnit;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class KafkaHealthCheckRepository {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaHealthCheckRepository(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void validateTemplate() {
        if (kafkaTemplate == null || kafkaTemplate.getProducerFactory() == null) {
            throw new IllegalStateException("KafkaTemplate is not configured");
        }
    }

    public void sendTestMessage(String topic, long timeoutMs) throws Exception {
        kafkaTemplate.send(topic, "health-check", "ping")
                .get(timeoutMs, TimeUnit.MILLISECONDS);
    }
}
