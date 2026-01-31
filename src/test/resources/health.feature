Feature: 인프라 헬스 체크 API
  인프라 구성요소(MySQL, Redis, Kafka)의 상태를 확인하는 API

  Background:
    Given 모든 인프라 컨테이너가 정상 실행 중이다

  Scenario: 전체 헬스 체크 - 모든 서비스 정상
    When 클라이언트가 GET /health 를 요청하면
    Then 응답 상태 코드는 200 이다
    And MySQL 상태는 "UP" 이다
    And Redis 상태는 "UP" 이다
    And Kafka 상태는 "UP" 이다
