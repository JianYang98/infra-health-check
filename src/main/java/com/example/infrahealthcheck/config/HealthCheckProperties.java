package com.example.infrahealthcheck.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "healthcheck")
public class HealthCheckProperties {

    private final Kafka kafka = new Kafka();

    public Kafka getKafka() {
        return kafka;
    }

    public static class Kafka {
        private boolean sendTestMessage = false;
        private String testTopic = "healthcheck";
        private long timeoutMs = 1000;

        public boolean isSendTestMessage() {
            return sendTestMessage;
        }

        public void setSendTestMessage(boolean sendTestMessage) {
            this.sendTestMessage = sendTestMessage;
        }

        public String getTestTopic() {
            return testTopic;
        }

        public void setTestTopic(String testTopic) {
            this.testTopic = testTopic;
        }

        public long getTimeoutMs() {
            return timeoutMs;
        }

        public void setTimeoutMs(long timeoutMs) {
            this.timeoutMs = timeoutMs;
        }
    }
}
