package org.example.data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds a typed grade dataset together with course lookup metadata.
 */
public final class GradeDataset {
    private final DatasetType type;
    private final List<String> courseNames;
    private final List<GradeRow> rows;
    private final Map<String, Integer> courseIndexByName;

    public GradeDataset(DatasetType type, List<String> courseNames, List<GradeRow> rows) {
        this.type = type;
        this.courseNames = List.copyOf(courseNames);
        this.rows = List.copyOf(rows);
        this.courseIndexByName = buildIndex(this.courseNames);
    }

    public DatasetType type() {
        return type;
    }

    public List<String> courseNames() {
        return courseNames;
    }

    public List<GradeRow> rows() {
        return rows;
    }

    public int courseIndex(String courseName) {
        return courseIndexByName.getOrDefault(courseName, -1);
    }

    private Map<String, Integer> buildIndex(List<String> names) {
        Map<String, Integer> index = new LinkedHashMap<>();
        for (int i = 0; i < names.size(); i++) {
            index.put(names.get(i), i);
        }
        return index;
    }
}
