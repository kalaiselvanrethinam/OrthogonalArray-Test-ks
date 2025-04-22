package org.hbn.oattesttool.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONException;

import java.io.File;
import java.io.FileWriter;
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

            // Generate the OA matrix
            List<List<String>> oaData = TaguchiOA.generateOA(numFactors, String.valueOf(rows));

            // Convert OA data to resolved combinations (List<Map<String, String>>)
            List<Map<String, String>> resolvedData = new ArrayList<>();
            for (List<String> row : oaData) {
                Map<String, String> rowMap = new LinkedHashMap<>();
                for (int i = 0; i < row.size(); i++) {
                    List<String> levels = factorLevels.get(headers.get(i));
                    int index = Integer.parseInt(row.get(i)) % levels.size();
                    rowMap.put(headers.get(i), levels.get(index));
                }
                resolvedData.add(rowMap);
            }

            // Generate JSON payloads
            List<String> jsonPayloads = JsonPayloadGenerator.generatePayloads(resolvedData);

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
            System.out.println("JSON created: " + jsonOutputDir.toAbsolutePath());

            // Generate Postman CSV
            String csvOutputPath = postmanOutputDir.resolve("postman_data.csv").toString();
            generateCSV(headers, resolvedData, csvOutputPath);
            System.out.println("Postman CSV created: " + csvOutputPath);

            System.out.println("Batch Execution Completed from GUI!");

        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println("Error occurred during batch execution from GUI.");
        }
    }

    private static void generateCSV(List<String> headers, List<Map<String, String>> combinations, String outputPath) throws IOException {
        FileWriter writer = new FileWriter(outputPath);
        writer.append(String.join(",", headers)).append("\n");

        for (Map<String, String> combination : combinations) {
            for (String header : headers) {
                writer.append(combination.getOrDefault(header, "")).append(",");
            }
            writer.append("\n");
        }

        writer.flush();
        writer.close();
    }
}
