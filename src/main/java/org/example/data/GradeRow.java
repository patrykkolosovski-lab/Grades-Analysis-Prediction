package org.example.data;

import java.util.List;

/**
 * Represents one student's grades across every course in a dataset.
 */
public record GradeRow(String studentId, List<Double> grades) {
}
