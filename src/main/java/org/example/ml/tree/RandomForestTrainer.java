package org.example.ml.tree;

import org.example.data.StudentProfile;
import org.example.ml.MlSample;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Trains an ensemble of small regression trees.
 */
public final class RandomForestTrainer {
    private final int treeCount;
    private final DecisionTreeTrainer decisionTreeTrainer;

    public RandomForestTrainer(int treeCount, DecisionTreeTrainer decisionTreeTrainer) {
        this.treeCount = treeCount;
        this.decisionTreeTrainer = decisionTreeTrainer;
    }

    /**
     * Trains a forest using bootstrap resampling.
     */
    public RandomForestModel train(List<MlSample> samples, long seed) {
        Random random = new Random(seed);
        List<DecisionTreeModel> trees = new ArrayList<>(treeCount);
        for (int i = 0; i < treeCount; i++) {
            List<MlSample> bootstrap = new ArrayList<>(samples.size());
            for (int j = 0; j < samples.size(); j++) {
                bootstrap.add(samples.get(random.nextInt(samples.size())));
            }
            trees.add(decisionTreeTrainer.train(bootstrap));
        }
        return new RandomForestModel(trees);
    }

    /**
     * Holds an ensemble of trained trees.
     */
    public record RandomForestModel(List<DecisionTreeModel> trees) {
        public double predict(StudentProfile profile) {
            double total = 0.0;
            for (DecisionTreeModel tree : trees) {
                total += tree.predict(profile);
            }
            return trees.isEmpty() ? Double.NaN : total / trees.size();
        }
    }
}
