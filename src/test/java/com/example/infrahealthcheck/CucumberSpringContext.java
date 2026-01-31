package com.example.infrahealthcheck;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = InfraHealthCheckApplication.class)
public class CucumberSpringContext {

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        // InfraContainers의 static 블록에서 컨테이너가 이미 시작됨
        // 여기서 Spring 프로퍼티에 동적으로 연결
        registry.add("spring.datasource.url", InfraContainers.MYSQL::getJdbcUrl);
        registry.add("spring.datasource.username", InfraContainers.MYSQL::getUsername);
        registry.add("spring.datasource.password", InfraContainers.MYSQL::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");

        registry.add("spring.data.redis.host", InfraContainers.REDIS::getHost);
        registry.add("spring.data.redis.port", () -> InfraContainers.REDIS.getMappedPort(6379));

        registry.add("spring.kafka.bootstrap-servers", InfraContainers.KAFKA::getBootstrapServers);
    }
}
