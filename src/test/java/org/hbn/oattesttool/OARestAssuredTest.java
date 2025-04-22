package org.hbn.oattesttool;

import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.*;

import static io.restassured.RestAssured.given;

public class OARestAssuredTest {

    private static final String BASE_URL = "http://localhost:8052/api/createAccount";

    @Test
    public void runAllGeneratedPayloads() throws IOException {
        Path payloadDir = Paths.get("output/json");

        try (DirectoryStream<Path> files = Files.newDirectoryStream(payloadDir, "*.json")) {
            for (Path file : files) {
                String content = Files.readString(file);
                JSONObject json = new JSONObject(content);

                Response response = given()
                        .header("Content-Type", "application/json")
                        .body(json.toString())
                        .when()
                        .post(BASE_URL)
                        .then()
                        .extract()
                        .response();

                // Log result to console
                System.out.println("🔹 Testing: " + file.getFileName());
                System.out.println("Status Code: " + response.getStatusCode());
                System.out.println("Response: " + response.getBody().asString());
                System.out.println("--------------------------------------------------");

                // Optional: Save to results folder
                Path resultFile = Paths.get("output/results", file.getFileName().toString().replace(".json", "_result.txt"));
                Files.createDirectories(resultFile.getParent());
                Files.writeString(resultFile,
                        "Status: " + response.getStatusCode() + "\n" +
                                "Response:\n" + response.getBody().asPrettyString());
            }
        }
    }
}
