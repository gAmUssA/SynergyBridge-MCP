package com.synergybridge;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class HealthInfoEndpointTest {

    @Test
    void healthEndpointReturnsJsonWith200() {
        given()
            .when().get("/")
            .then()
                .statusCode(200)
                .contentType("application/json")
                .body("name", equalTo("SynergyBridge MCP"))
                .body("javaVersion", notNullValue())
                .body("jvmInfo", notNullValue())
                .body("javaVendor", notNullValue());
    }
}
