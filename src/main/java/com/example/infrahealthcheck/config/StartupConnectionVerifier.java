package com.example.infrahealthcheck.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StartupConnectionVerifier implements ApplicationRunner {
    private final DataSource dataSource;
    private final RedisConnectionFactory redisConnectionFactory;
    private final KafkaAdmin kafkaAdmin;

    @Override
    public void run(ApplicationArguments args) {
        verifyMySql();
        verifyRedis();
        verifyKafka();
    }

    private void verifyMySql() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT 1")) {
            statement.execute();
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to connect to MySQL", ex);
        }
    }

    private void verifyRedis() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            String pong = connection.ping();
            if (pong == null || pong.isBlank()) {
                throw new IllegalStateException("Redis ping returned empty response");
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to connect to Redis", ex);
        }
    }

    private void verifyKafka() {
        try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
            DescribeClusterResult result = adminClient.describeCluster();
            String clusterId = result.clusterId().get(3, TimeUnit.SECONDS);
            if (clusterId == null || clusterId.isBlank()) {
                throw new IllegalStateException("Kafka cluster id is empty");
            }
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to connect to Kafka", ex);
        }
    }
}
