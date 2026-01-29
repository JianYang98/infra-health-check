package com.example.infrahealthcheck.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseHealthCheckRepository {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseHealthCheckRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Integer check() {
        return jdbcTemplate.queryForObject("SELECT 1", Integer.class);
    }
}
