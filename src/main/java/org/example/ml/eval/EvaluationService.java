package org.example.ml.eval;

import org.example.ml.ForestModelConfig;
import org.example.ml.MlSample;
import org.example.ml.tree.DecisionTreeTrainer;
import org.example.ml.tree.RandomForestTrainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Runs repeatable forest validation over aligned ML samples.
 */
public final class EvaluationService {
    private final ForestModelConfig config;

    public EvaluationService(ForestModelConfig config) {
        this.config = config;
    }

    /**
     * Trains a forest on an 80/20 deterministic holdout split and returns validation MSE.
     */
    public double evaluate(List<MlSample> samples) {
        List<MlSample> copy = new ArrayList<>(samples);
        Collections.shuffle(copy, new java.util.Random(config.seed()));
        int split = Math.max(1, (int) Math.round(copy.size() * 0.8));
        List<MlSample> train = copy.subList(0, split);
        List<MlSample> validation = copy.subList(split, copy.size());
        if (validation.isEmpty()) {
            validation = train;
        }

        DecisionTreeTrainer trainer = new DecisionTreeTrainer(config.maxDepth(), config.minSamplesLeaf());
        RandomForestTrainer forestTrainer = new RandomForestTrainer(config.treeCount(), trainer);
        RandomForestTrainer.RandomForestModel forest = forestTrainer.train(train, config.seed());

        double sumSquaredError = 0.0;
        for (MlSample sample : validation) {
            double error = forest.predict(sample.profile()) - sample.targetGrade();
            sumSquaredError += error * error;
        }
        return validation.isEmpty() ? Double.NaN : sumSquaredError / validation.size();
    }
}
