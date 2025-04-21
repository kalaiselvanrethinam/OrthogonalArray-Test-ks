package org.hbn.oattesttool.core;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.io.File;

public class JsonPayloadGenerator {




        private static final String OUTPUT_DIR = "output/json";

        public static List<String> generatePayloads(List<Map<String, String>> combinations) throws IOException {
            File dir = new File(OUTPUT_DIR);
            if (!dir.exists()) dir.mkdirs();

            List<String> payloads = new ArrayList<>(); // ✅ Declare payloads list

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
                jsonBuilder.append("\n}");

                String json = jsonBuilder.toString();
                payloads.add(json); // ✅ Add to list

                try (FileWriter file = new FileWriter(OUTPUT_DIR + "/payload_" + count++ + ".json")) {
                    file.write(json);
                }
            }

            return payloads; // ✅ Now returns the built list
        }
    }
