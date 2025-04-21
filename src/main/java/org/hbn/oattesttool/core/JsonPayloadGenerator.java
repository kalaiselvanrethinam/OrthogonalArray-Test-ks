package org.hbn.oattesttool.core;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.io.File;

public class JsonPayloadGenerator {

    private static final String OUTPUT_DIR = "output/json";

    public static void generatePayloads(List<Map<String, String>> combinations) throws IOException {
        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) dir.mkdirs();

        int count = 1;
        for (Map<String, String> combination : combinations) {
            StringBuilder jsonBuilder = new StringBuilder("{\n");
            for (Map.Entry<String, String> entry : combination.entrySet()) {
                jsonBuilder.append("  \"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\",\n");
            }
            // Remove last comma
            int lastComma = jsonBuilder.lastIndexOf(",");
            if (lastComma != -1) {
                jsonBuilder.deleteCharAt(lastComma);
            }
            jsonBuilder.append("}");

            try (FileWriter file = new FileWriter(OUTPUT_DIR + "/payload_" + count++ + ".json")) {
                file.write(jsonBuilder.toString());
            }
        }
    }
}
