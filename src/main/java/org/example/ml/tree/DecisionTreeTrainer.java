package org.example.ml.tree;

import org.example.analytics.FeatureType;
import org.example.ml.MlDatasetAdapter;
import org.example.ml.MlSample;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Trains a small regression tree over the Current student profile dataset.
 */
public final class DecisionTreeTrainer {
    private final int maxDepth;
    private final int minSamplesLeaf;

    public DecisionTreeTrainer(int maxDepth, int minSamplesLeaf) {
        this.maxDepth = maxDepth;
        this.minSamplesLeaf = minSamplesLeaf;
    }

    /**
     * Trains a tree from aligned samples.
     */
    public DecisionTreeModel train(List<MlSample> samples) {
        if (samples.isEmpty()) {
            throw new IllegalArgumentException("Training samples must not be empty");
        }
        return new DecisionTreeModel(build(samples, 0));
    }

    private PredictionNode build(List<MlSample> samples, int depth) {
        double mean = mean(samples);
        if (depth >= maxDepth || samples.size() <= minSamplesLeaf * 2) {
            return new LeafNode(mean, samples.size());
        }

        Split best = bestSplit(samples);
        if (best == null || best.left().size() < minSamplesLeaf || best.right().size() < minSamplesLeaf) {
            return new LeafNode(mean, samples.size());
        }

        return new DecisionNode(
                best.featureType(),
                best.threshold(),
                build(best.left(), depth + 1),
                build(best.right(), depth + 1),
                mean
        );
    }

    private Split bestSplit(List<MlSample> samples) {
        Split best = null;
        for (FeatureType featureType : List.of(
                FeatureType.QUANTUM_COHERENCE_THRESHOLD,
                FeatureType.SYMBIOTIC_NETWORK_COMPATIBILITY,
                FeatureType.ASTRO_TEMPORAL_DRIFT_RESISTANCE,
                FeatureType.PSIONIC_INTERFERENCE_TOLERANCE,
                FeatureType.BIO_LUMINAL_TRANSMISSION
        )) {
            List<Double> values = new ArrayList<>(samples.size());
            for (MlSample sample : samples) {
                values.add(MlDatasetAdapter.featureValue(sample.profile(), featureType));
            }
            values.sort(Comparator.naturalOrder());
            for (int i = 1; i < values.size(); i++) {
                double leftValue = values.get(i - 1);
                double rightValue = values.get(i);
                if (leftValue == rightValue) {
                    continue;
                }
                double threshold = (leftValue + rightValue) / 2.0;
                Split split = split(samples, featureType, threshold);
                if (split.left().isEmpty() || split.right().isEmpty()) {
                    continue;
                }
                if (best == null || split.score() < best.score()) {
                    best = split;
                }
            }
        }
        return best;
    }

    private Split split(List<MlSample> samples, FeatureType featureType, double threshold) {
        List<MlSample> left = new ArrayList<>();
        List<MlSample> right = new ArrayList<>();
        for (MlSample sample : samples) {
            double value = MlDatasetAdapter.featureValue(sample.profile(), featureType);
            if (value >= threshold) {
                left.add(sample);
            } else {
                right.add(sample);
            }
        }
        return new Split(featureType, threshold, left, right, weightedVariance(left, right));
    }

    private double weightedVariance(List<MlSample> left, List<MlSample> right) {
        int total = left.size() + right.size();
        return (left.size() * variance(left) + right.size() * variance(right)) / total;
    }

    private double mean(List<MlSample> samples) {
        double sum = 0.0;
        for (MlSample sample : samples) {
            sum += sample.targetGrade();
        }
        return sum / samples.size();
    }

    private double variance(List<MlSample> samples) {
        if (samples.size() <= 1) {
            return 0.0;
        }
        double mean = mean(samples);
        double total = 0.0;
        for (MlSample sample : samples) {
            double delta = sample.targetGrade() - mean;
            total += delta * delta;
        }
        return total / samples.size();
    }

    /**
     * Holds candidate split state during training.
     */
    public record Split(FeatureType featureType, double threshold, List<MlSample> left, List<MlSample> right, double score) {
    }
}
