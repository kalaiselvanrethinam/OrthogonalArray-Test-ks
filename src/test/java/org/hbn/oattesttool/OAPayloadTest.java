package org.hbn.oattesttool;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OAPayloadTest {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080; // default Spring Boot port
    }

    @Test
    public void testOAPayloads() throws Exception {
        File folder = new File("src/test/resources/generated_payloads");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        assert files != null;
        for (File file : files) {
            String jsonPayload = new String(FileCopyUtils.copyToByteArray(file), StandardCharsets.UTF_8);

            given()
                    .contentType("application/json")
                    .body(jsonPayload)
                    .when()
                    .post("/api/login")
                    .then()
                    .statusCode(200)
                    .body("message", equalTo("Login simulated"));
        }
    }
}
