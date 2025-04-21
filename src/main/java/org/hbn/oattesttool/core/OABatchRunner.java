package org.hbn.oattesttool.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class OABatchRunner {

    public static void runBatch(String oaType, LinkedHashMap<String, List<String>> factorLevels) {
        try {
            int numFactors = factorLevels.size();
            List<String> headers = new ArrayList<>(factorLevels.keySet());

            // Determine OA rows from OA type
            int rows = switch (oaType.toUpperCase()) {
                case "L4" -> 4;
                case "L8" -> 8;
                case "L16" -> 16;
                default -> throw new IllegalArgumentException("Unsupported OA Type: " + oaType);
            };

            List<List<String>> oaData = TaguchiOA.generateOA(numFactors, rows);

            // Map OA index values to actual level values
            List<List<String>> resolvedData = new ArrayList<>();
            for (List<String> row : oaData) {
                List<String> resolvedRow = new ArrayList<>();
                for (int i = 0; i < row.size(); i++) {
                    List<String> levels = factorLevels.get(headers.get(i));
                    int index = Integer.parseInt(row.get(i)) % levels.size();
                    resolvedRow.add(levels.get(index));
                }
                resolvedData.add(resolvedRow);
            }

            // Generate JSON payloads
            List<String> jsonPayloads = JsonPayloadGenerator.generatePayloads(resolvedData, headers);

            // Output directories
            Path jsonOutputDir = Paths.get("output/json");
            Path postmanOutputDir = Paths.get("output/postman");

            Files.createDirectories(jsonOutputDir);
            Files.createDirectories(postmanOutputDir);

            // Write JSON files
            ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            File outputFile = null;
            for (int i = 0; i < jsonPayloads.size(); i++) {
                String json = jsonPayloads.get(i);
                outputFile = new File(jsonOutputDir.toFile(), "payload_" + (i + 1) + ".json");
                mapper.writeValue(outputFile, mapper.readTree(json));
            }
            System.out.println("JSON created: " + outputFile.getAbsolutePath());

            // Generate Postman CSV
            String csvOutputPath = postmanOutputDir.resolve("postman_data.csv").toString();
            PostmanCSVGenerator.generateCSV(headers, resolvedData, csvOutputPath);

            System.out.println("PostMan CSV  created: " + outputFile.getAbsolutePath());

            System.out.println("Batch Execution Completed from GUI!");
        } catch (IOException | JSONException | IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Error occurred during batch execution from GUI.");
        }
    }
}
