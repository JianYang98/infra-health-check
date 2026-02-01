# ===== Build Stage =====
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# Gradle wrapper 먼저 복사 (캐싱 최적화)
COPY gradlew .
COPY gradle gradle
RUN chmod +x gradlew

# 의존성 파일 먼저 복사 → 변경 없으면 캐시 재사용
COPY build.gradle* settings.gradle* ./
RUN ./gradlew dependencies --no-daemon || true

# 소스 복사 및 빌드
COPY src src
RUN ./gradlew clean bootJar -x test --no-daemon

# ===== Runtime Stage =====
FROM eclipse-temurin:17-jre

WORKDIR /app

# 빌드된 JAR만 복사
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
