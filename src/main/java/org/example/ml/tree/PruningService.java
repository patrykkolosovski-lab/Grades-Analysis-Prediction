package org.example.ml.tree;

import org.example.ml.MlSample;

import java.util.List;

/**
 * Provides a lightweight pruning pass that collapses shallow branches when validation error does not improve.
 */
public final class PruningService {
    private PruningService() {
    }

    /**
     * Returns the original tree or a leaf if a flat predictor is at least as good on validation data.
     */
    public static DecisionTreeModel prune(DecisionTreeModel model, List<MlSample> validationSamples) {
        double treeMse = mse(model, validationSamples);
        double mean = validationSamples.stream().mapToDouble(MlSample::targetGrade).average().orElse(0.0);
        DecisionTreeModel flatModel = new DecisionTreeModel(new LeafNode(mean, validationSamples.size()));
        return mse(flatModel, validationSamples) <= treeMse ? flatModel : model;
    }

    public static double mse(DecisionTreeModel model, List<MlSample> samples) {
        double sum = 0.0;
        for (MlSample sample : samples) {
            double error = model.predict(sample.profile()) - sample.targetGrade();
            sum += error * error;
        }
        return samples.isEmpty() ? Double.NaN : sum / samples.size();
    }
}
