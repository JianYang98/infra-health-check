package com.example.infrahealthcheck.dto;

public record DependencyStatus(String status, String message, long responseTimeMs) {
}
