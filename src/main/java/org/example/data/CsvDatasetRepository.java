package org.example.data;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Loads the bundled CSV resources into typed application datasets.
 */
public final class CsvDatasetRepository implements DatasetRepository {
    private static final String CURRENT_GRADES = "/CurrentGrades.csv";
    private static final String GRADUATE_GRADES = "/GraduateGrades.csv";
    private static final String STUDENT_INFO = "/StudentInfo.csv";

    @Override
    public AppData load() throws Exception {
        GradeDataset current = loadGradeDataset(DatasetType.CURRENT, CURRENT_GRADES);
        GradeDataset graduate = loadGradeDataset(DatasetType.GRADUATE, GRADUATE_GRADES);
        Map<String, StudentProfile> profiles = loadStudentProfiles(STUDENT_INFO);
        return new AppData(current, graduate, Map.copyOf(profiles));
    }

    /**
     * Exposes resource loading for tests and adapters.
     */
    public GradeDataset loadGradeDataset(DatasetType datasetType, String resourcePath) throws Exception {
        try (BufferedReader reader = open(resourcePath)) {
            String header = reader.readLine();
            if (header == null) {
                throw new IllegalStateException("Missing header in " + resourcePath);
            }

            List<String> columns = parseLine(header);
            if (columns.size() < 2) {
                throw new IllegalStateException("Expected student id plus course columns in " + resourcePath);
            }

            List<String> courseNames = columns.subList(1, columns.size());
            List<GradeRow> rows = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> raw = parseLine(line);
                if (raw.isEmpty()) {
                    continue;
                }
                String studentId = raw.getFirst().trim();
                List<Double> grades = new ArrayList<>(courseNames.size());
                for (int i = 1; i <= courseNames.size(); i++) {
                    String token = i < raw.size() ? raw.get(i) : "";
                    grades.add(parseNullableDouble(token));
                }
                rows.add(new GradeRow(studentId, Collections.unmodifiableList(new ArrayList<>(grades))));
            }
            return new GradeDataset(datasetType, List.copyOf(courseNames), rows);
        }
    }

    /**
     * Exposes profile loading for tests and adapters.
     */
    public Map<String, StudentProfile> loadStudentProfiles(String resourcePath) throws Exception {
        try (BufferedReader reader = open(resourcePath)) {
            String header = reader.readLine();
            if (header == null) {
                throw new IllegalStateException("Missing header in " + resourcePath);
            }

            Map<String, StudentProfile> profiles = new LinkedHashMap<>();
            String line;
            while ((line = reader.readLine()) != null) {
                List<String> raw = parseLine(line);
                if (raw.size() < 6) {
                    continue;
                }
                StudentProfile profile = new StudentProfile(
                        raw.get(0).trim(),
                        raw.get(1).trim(),
                        raw.get(2).trim(),
                        raw.get(3).trim(),
                        parseRequiredDouble(raw.get(4)),
                        raw.get(5).trim()
                );
                profiles.put(profile.studentId(), profile);
            }
            return profiles;
        }
    }

    private BufferedReader open(String resourcePath) {
        InputStream stream = CsvDatasetRepository.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new IllegalStateException("Resource not found: " + resourcePath);
        }
        return new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    private List<String> parseLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"') {
                quoted = !quoted;
            } else if (ch == ',' && !quoted) {
                tokens.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }
        tokens.add(current.toString());
        return tokens;
    }

    private Double parseNullableDouble(String token) {
        String normalized = token == null ? "" : token.trim();
        if (normalized.isEmpty() || normalized.equalsIgnoreCase("NG")) {
            return null;
        }
        return Double.parseDouble(normalized);
    }

    private double parseRequiredDouble(String token) {
        String normalized = token == null ? "" : token.trim();
        if (normalized.isEmpty()) {
            throw new IllegalStateException("Expected numeric value");
        }
        return Double.parseDouble(normalized);
    }
}
