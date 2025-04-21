package org.hbn.oattesttool.core;

import java.util.*;

public class TaguchiOA {

    private static final Map<String, List<List<String>>> baseOAs = Map.of(
            "L4", List.of(
                    List.of("0", "0", "0"),
                    List.of("0", "1", "1"),
                    List.of("1", "0", "1"),
                    List.of("1", "1", "0")
            ),
            "L8", List.of(
                    List.of("0", "0", "0", "0"),
                    List.of("0", "1", "1", "1"),
                    List.of("1", "0", "1", "1"),
                    List.of("1", "1", "0", "0"),
                    List.of("0", "0", "1", "0"),
                    List.of("0", "1", "0", "1"),
                    List.of("1", "0", "0", "1"),
                    List.of("1", "1", "1", "0")
            ),
            "L16", List.of(
                    List.of("0", "0", "0", "0"),
                    List.of("0", "1", "1", "1"),
                    List.of("1", "0", "1", "1"),
                    List.of("1", "1", "0", "0"),
                    List.of("0", "0", "1", "0"),
                    List.of("0", "1", "0", "1"),
                    List.of("1", "0", "0", "1"),
                    List.of("1", "1", "1", "0"),
                    List.of("0", "0", "0", "1"),
                    List.of("0", "1", "1", "0"),
                    List.of("1", "0", "1", "0"),
                    List.of("1", "1", "0", "1"),
                    List.of("0", "0", "1", "1"),
                    List.of("0", "1", "0", "0"),
                    List.of("1", "0", "0", "0"),
                    List.of("1", "1", "1", "1")
            )
    );

    public static List<List<String>> generateOA(int numFactors, String oaType) {
        List<List<String>> baseOA = baseOAs.get(oaType.toUpperCase());
        if (baseOA == null) {
            throw new IllegalArgumentException("Unsupported OA type: " + oaType);
        }

        int baseCols = baseOA.get(0).size();
        if (numFactors <= baseCols) {
            // Trim to required number of columns
            List<List<String>> trimmed = new ArrayList<>();
            for (List<String> row : baseOA) {
                trimmed.add(row.subList(0, numFactors));
            }
            return trimmed;
        } else {
            // Repeat columns to match number of factors
            List<List<String>> extended = new ArrayList<>();
            for (List<String> row : baseOA) {
                List<String> newRow = new ArrayList<>();
                for (int i = 0; i < numFactors; i++) {
                    String val = row.get(i % baseCols); // cycle through base columns
                    newRow.add(val);
                }
                extended.add(newRow);
            }
            return extended;
        }
    }

    public static List<Map<String, String>> generateOACombinations(LinkedHashMap<String, List<String>> factorLevels, String oaType) {
        List<String> factorNames = new ArrayList<>(factorLevels.keySet());
        List<List<String>> oaMatrix = generateOA(factorLevels.size(), oaType);

        List<Map<String, String>> resolved = new ArrayList<>();
        for (List<String> row : oaMatrix) {
            Map<String, String> combination = new LinkedHashMap<>();
            for (int i = 0; i < factorNames.size(); i++) {
                String factor = factorNames.get(i);
                List<String> levels = factorLevels.get(factor);
                int levelIndex = Integer.parseInt(row.get(i)) % levels.size();
                combination.put(factor, levels.get(levelIndex));
            }
            resolved.add(combination);
        }

        return resolved;
    }
}
