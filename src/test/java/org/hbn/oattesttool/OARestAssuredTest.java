package org.hbn.oattesttool;

import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.json.JSONException;
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

                // Log test start to Allure report
                Allure.step("Sending request for payload: " + file.getFileName());

                // Sending the request
                Response response = given()
                        .header("Content-Type", "application/json")
                        .body(json.toString())
                        .when()
                        .post(BASE_URL)
                        .then()
                        .extract()
                        .response();

                // Log result to console (this will also be included in the report)
                System.out.println("🔹 Testing: " + file.getFileName());
                System.out.println("Status Code: " + response.getStatusCode());
                System.out.println("Response: " + response.getBody().asString());
                System.out.println("--------------------------------------------------");

                // Add results to Allure
                Allure.step("Status Code: " + response.getStatusCode());
                Allure.step("Response Body: " + response.getBody().asString());

                // Optional: Save to results folder
                Path resultFile = Paths.get("output/results", file.getFileName().toString().replace(".json", "_result.txt"));
                Files.createDirectories(resultFile.getParent());
                Files.writeString(resultFile,
                        "Status: " + response.getStatusCode() + "\n" +
                                "Response:\n" + response.getBody().asPrettyString());
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
