package com.example.infrahealthcheck;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class InfraContainers {

    public static final MySQLContainer<?> MYSQL;
    public static final GenericContainer<?> REDIS;
    public static final KafkaContainer KAFKA;

    static {
        // MySQL 컨테이너
        MYSQL = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("health")
            .withUsername("health_user")
            .withPassword("health1234");

        // Redis 컨테이너
        REDIS = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
            .withExposedPorts(6379);

        // Kafka 컨테이너
        KAFKA = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));

        // 모든 컨테이너 시작
        MYSQL.start();
        REDIS.start();
        KAFKA.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        // MySQL 설정
        registry.add("spring.datasource.url", MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL::getUsername);
        registry.add("spring.datasource.password", MYSQL::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");

        // Redis 설정
        registry.add("spring.data.redis.host", REDIS::getHost);
        registry.add("spring.data.redis.port", () -> REDIS.getMappedPort(6379));

        // Kafka 설정
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
    }
}
