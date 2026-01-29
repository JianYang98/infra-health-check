package com.example.infrahealthcheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class InfraHealthCheckApplication {

    public static void main(String[] args) {
        SpringApplication.run(InfraHealthCheckApplication.class, args);
    }
}
