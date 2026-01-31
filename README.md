##   Infra Health Check Project

이 프로젝트는 MySQL, Redis, Kafka와 같은 핵심 인프라의 상태를 점검하기 위한 백엔드 헬스 체크 시스템입니다.

Docker 기반 환경에서 인프라를 구성하고, Spring Boot 애플리케이션을 통해 각 시스템의 연결 상태를 확인할 수 있습니다.

---

##   How to Run

프로젝트를 클론한 후 프로젝트 루트 디렉토리로 이동합니다.

### 1 -2 Full Docker Environment

```bash
docker compose -f docker-compose.yml -f docker-compose.app.yml up -d --build
```
### 1-2 Infra + Local App (Dev Mode)

```bash
docker compose up -d
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

## Stack
- Java 17
- Spring Boot 3.x
- Gradle
- MySQL, Redis, Kafka

 