package com.example.infrahealthcheck;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.restassured.response.Response;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class HealthSteps {

    @LocalServerPort
    private int port;

    private Response response;

    @Given("모든 인프라 컨테이너가 정상 실행 중이다")
    public void 모든_인프라_컨테이너가_정상_실행_중이다() {
        assertThat(InfraContainers.MYSQL.isRunning()).isTrue();
        assertThat(InfraContainers.REDIS.isRunning()).isTrue();
        assertThat(InfraContainers.KAFKA.isRunning()).isTrue();
    }

    @When("클라이언트가 GET \\/health 를 요청하면")
    public void 클라이언트가_GET_health를_요청하면() {
        response = given()
            .port(port)
            .when()
            .get("/health")
            .andReturn();
    }

    @Then("응답 상태 코드는 {int} 이다")
    public void 응답_상태_코드는_이다(int expectedStatus) {
        assertThat(response.getStatusCode()).isEqualTo(expectedStatus);
    }

    @And("MySQL 상태는 {string} 이다")
    public void mysql_상태는_이다(String expectedStatus) {
        String status = response.jsonPath().getString("mysql.status");
        assertThat(status).isEqualTo(expectedStatus);
    }

    @And("Redis 상태는 {string} 이다")
    public void redis_상태는_이다(String expectedStatus) {
        String status = response.jsonPath().getString("redis.status");
        assertThat(status).isEqualTo(expectedStatus);
    }

    @And("Kafka 상태는 {string} 이다")
    public void kafka_상태는_이다(String expectedStatus) {
        String status = response.jsonPath().getString("kafka.status");
        assertThat(status).isEqualTo(expectedStatus);
    }
}
