package com.example.infrahealthcheck.repository;

import com.example.infrahealthcheck.model.HealthCheckStatus;
import java.sql.Connection;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DbHealthRepository {
    private final DataSource dataSource;

    public HealthCheckStatus check() {
        long start = System.nanoTime();
        try (Connection connection = dataSource.getConnection()) {
            boolean valid = connection.isValid(2);
            return HealthCheckStatus.builder()
                .status(valid ? "UP" : "DOWN")
                .details(valid ? "MySQL connection OK" : "MySQL connection invalid")
                .responseTimeMs(elapsedMs(start))
                .build();
        } catch (Exception ex) {
            return HealthCheckStatus.builder()
                .status("DOWN")
                .details("MySQL error: " + safeMessage(ex))
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