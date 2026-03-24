package org.example.analytics;

import org.example.data.StudentProfile;

/**
 * Represents the absence of profile-based filtering.
 */
public final class NoFilter implements FeatureFilter {
    @Override
    public boolean matches(StudentProfile profile) {
        return true;
    }

    @Override
    public String description() {
        return "No filter";
    }
}
