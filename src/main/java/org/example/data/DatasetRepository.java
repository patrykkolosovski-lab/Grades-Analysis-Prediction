package org.example.data;

/**
 * Loads application datasets from persistent storage.
 */
public interface DatasetRepository {
    AppData load() throws Exception;
}
