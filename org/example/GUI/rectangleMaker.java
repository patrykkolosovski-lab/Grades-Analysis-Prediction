package org.example.GUI;

import javafx.scene.shape.Rectangle;
import java.util.Arrays;

public class rectangleMaker {

    public Rectangle[] createMarginalHistograms(
            double[] values,
            double minVal,
            double maxVal,
            double width,
            double height,
            boolean vertical // true = vertical bars (top histogram), false = horizontal bars (right histogram)
    ) {

        int bins = 10;
        double binSize = (maxVal - minVal) / bins;

        int[] counts = new int[bins];

        // --- Count how many fall into each bin ---
        for (double v : values) {
            int idx = (int) ((v - minVal) / binSize);
            if (idx < 0) idx = 0;
            if (idx >= bins) idx = bins - 1;
            counts[idx]++;
        }

        // Normalize heights
        int maxCount = Arrays.stream(counts).max().orElse(1);

        Rectangle[] rects = new Rectangle[bins];

        for (int i = 0; i < bins; i++) {
            Rectangle r = new Rectangle();

            if (vertical) {
                // top histogram: rectangles extend DOWN
                double barWidth = width / bins + 3;
                double barHeight = (counts[i] / (double) maxCount) * height;

                r.setX(i * barWidth + 43);
                r.setY(height - barHeight);
                r.setWidth(barWidth - 2);
                r.setHeight(barHeight);
            } else {
                // right histogram: rectangles extend LEFT
                double barHeight = height / bins + 3;
                double barWidth = (counts[i] / (double) maxCount) * width;

                r.setX(0);
                // Reverse the order so index 0 is at the bottom, index bins-1 at the top
                r.setY((bins - 1 - i) * barHeight);

                r.setWidth(barWidth);
                r.setHeight(barHeight - 2);
            }

            r.setStyle("-fx-fill: rgba(198,76,0,0.8); -fx-stroke: black; -fx-stroke-width: 0.5;");
            rects[i] = r;
        }

        return rects;
    }
}
