# GitHub Actions CI Workflow 구축 계획

## 1. GitHub Actions 핵심 개념 (5줄 요약)

| 개념 | 설명 |
|------|------|
| **Workflow** | `.github/workflows/*.yml`에 정의된 자동화 프로세스. 특정 이벤트(push, PR 등)에 트리거됨 |
| **Job** | Workflow 내에서 실행되는 작업 단위. 각 Job은 독립된 Runner(가상머신)에서 실행됨 |
| **Step** | Job 내의 개별 실행 단계. Action 사용 또는 shell 명령어 실행 가능 |

---

## 2. 우리가 만들 Workflow 구조

```
.github/workflows/ci.yml
```

### 2.1 트리거 조건
```yaml
on:
  push:
    branches: [ main ]
```
- `main` 브랜치에 push 시 자동 실행

### 2.2 Job 구성: `test`

| Step | Action/Command | 설명 |
|------|----------------|------|
| 1 | `actions/checkout@v4` | 소스코드 체크아웃 |
| 2 | `actions/setup-java@v4` | Java 17 + Temurin 배포판 설정 |
| 3 | `./gradlew test` | Cucumber + Testcontainers 테스트 실행 |
| 4 | (선택) `publish-unit-test-result-action` | JUnit 테스트 리포트 게시 |

---

## 3. 핵심 질문에 대한 답변

### Q1: docker-compose 없이 Testcontainers가 어떻게 동작하나?

**GitHub Actions Runner에는 Docker가 기본 설치되어 있음!**

```
GitHub-hosted Runner (ubuntu-latest) 기본 포함:
- Docker Engine
- Docker Compose
- 다양한 런타임 (Java, Node, Python 등)
```

Testcontainers는 `docker-compose.yml`이 아닌 **Docker API를 직접 호출**하여 컨테이너를 관리함.
따라서 Docker만 있으면 동작함.

### Q2: CI 서버에서 테스트가 느려지는 원인과 해결책

**원인:**
- GitHub 무료 Runner: 2 vCPU, 7GB RAM (로컬 대비 낮은 사양)
- 여러 컨테이너(MySQL, Kafka, Redis) 동시 실행 시 리소스 부족
- 컨테이너 이미지 pull 시간

**해결책:**
1. **Gradle 캐싱** - `actions/cache` 또는 `setup-java`의 캐시 옵션 활용
2. **병렬 실행 제한** - Testcontainers 동시 실행 수 조절
3. **타임아웃 여유있게 설정** - 컨테이너 시작 대기 시간 증가
4. **경량 이미지 사용** - alpine 기반 이미지 고려

---

## 4. 구현 계획 (Step by Step)

### Phase 1: 기본 CI Workflow 생성
- [ ] `.github/workflows/ci.yml` 파일 생성
- [ ] checkout, setup-java, gradlew test 구성
- [ ] Gradle 캐싱 설정 (빌드 속도 개선)

### Phase 2: 테스트 리포트 게시 (선택)
- [ ] `publish-unit-test-result-action` 추가
- [ ] 테스트 결과 GitHub UI에서 확인 가능하도록 설정

### Phase 3: 검증
- [ ] main 브랜치에 push하여 workflow 동작 확인
- [ ] Actions 탭에서 성공/실패 확인

---

## 5. 예상 ci.yml 구조

```yaml
name: CI

on:
  push:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v4

      - name: Set up Java 17
        uses: actions/setup-java@v4
        with:
          distribution: 'temurin'
          java-version: '17'
          cache: 'gradle'  # Gradle 의존성 캐싱

      - name: Grant execute permission for gradlew
        run: chmod +x gradlew

      - name: Run tests
        run: ./gradlew test

      # (선택) 테스트 리포트 게시
      - name: Publish Test Results
        uses: EnricoMi/publish-unit-test-result-action@v2
        if: always()  # 테스트 실패해도 리포트는 게시
        with:
          files: build/test-results/test/*.xml
```

---

## 6. 논의 필요 사항

1. **테스트 리포트 게시** - 선택 과제인데 포함할까요?
2. **PR 트리거 추가** - push 외에 PR에서도 테스트 실행할까요?
3. **추가 캐싱** - Docker 이미지 캐싱도 적용할까요?

---

## 다음 단계

계획이 괜찮다면 `.github/workflows/ci.yml` 파일을 생성하겠습니다!
