## Infra Health Check Project

이 프로젝트는 MySQL, Redis, Kafka와 같은 핵심 인프라의 상태를 점검하기 위한 백엔드 헬스 체크 시스템입니다.

Docker 기반 환경에서 인프라를 구성하고, Spring Boot 애플리케이션을 통해 각 시스템의 연결 상태를 확인할 수 있습니다.

---

## How to Run

프로젝트를 클론한 후 프로젝트 루트 디렉토리로 이동합니다.

### 1-1 Full Docker Environment

```bash
docker compose up -d --build
```

### 1-2 Infra + Local App (Dev Mode)

인프라만 실행하고 앱은 로컬에서 실행:
```bash
docker compose up -d mysql redis kafka
./gradlew bootRun
```

### 2 Health Check

애플리케이션 실행 후 아래 endpoint로 상태를 확인할 수 있습니다.
```bash
http://localhost:8080/health
```

### 3 Test

```bash
./gradlew test
```
Testcontainers가 테스트용 MySQL, Redis, Kafka를 자동으로 띄우고 테스트 합니다.

---

## Stack

- Java 17
- Spring Boot 3.3.7
- Gradle
- MySQL 8.0
- Redis 7 (Alpine)
- Kafka (KRaft 모드)

---

## Docker 구성

| 서비스 | 이미지 | 포트 |
|--------|--------|------|
| app | (Dockerfile 빌드) | 8080 |
| mysql | mysql:8.0 | 3306 |
| redis | redis:7-alpine | 6379 |
| kafka | confluentinc/cp-kafka:7.5.0 | 9092 |

### Dockerfile 최적화

- 멀티 스테이지 빌드 (빌드/런타임 분리)
- JRE 기반 런타임 이미지
- 레이어 캐싱 최적화
