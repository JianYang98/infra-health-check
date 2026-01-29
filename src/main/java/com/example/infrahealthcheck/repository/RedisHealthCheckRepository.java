package com.example.infrahealthcheck.repository;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class RedisHealthCheckRepository {

    private final RedisConnectionFactory connectionFactory;

    public RedisHealthCheckRepository(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public String ping() {
        try (RedisConnection connection = connectionFactory.getConnection()) {
            return connection.ping();
        }
    }
}
