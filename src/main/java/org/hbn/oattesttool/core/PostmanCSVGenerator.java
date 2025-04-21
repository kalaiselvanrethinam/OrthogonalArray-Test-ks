package org.hbn.oattesttool.core;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.io.File;

public class PostmanCSVGenerator {

    private static final String OUTPUT_DIR = "output/postman";

    public static void generateCSV(List<Map<String, String>> combinations, LinkedHashMap<String, List<String>> factorLevels) throws IOException {
        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) dir.mkdirs();

        FileWriter writer = new FileWriter(OUTPUT_DIR + "/postman_data.csv");

        // Dynamic headers
        List<String> headers = factorLevels.keySet().stream().toList();
        writer.append(String.join(",", headers)).append("\n");

        for (Map<String, String> combination : combinations) {
            for (String key : headers) {
                writer.append(combination.getOrDefault(key, "")).append(",");
            }
            writer.append("\n");
        }

        writer.flush();
        writer.close();
    }
}
