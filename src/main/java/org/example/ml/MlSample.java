package org.example.ml;

import org.example.data.StudentProfile;

/**
 * Represents one supervised learning example.
 */
public record MlSample(StudentProfile profile, double targetGrade) {
}
