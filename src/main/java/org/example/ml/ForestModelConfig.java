package org.example.ml;

/**
 * Defines the shared forest configuration used by prediction and evaluation.
 */
public record ForestModelConfig(
        int treeCount,
        int maxDepth,
        int minSamplesLeaf,
        long seed
) {
    public static final ForestModelConfig DEFAULT = new ForestModelConfig(25, 4, 20, 17L);
}
