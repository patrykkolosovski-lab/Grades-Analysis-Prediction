package org.example.ui;

import javafx.scene.Parent;
import org.example.data.AppData;
import org.example.data.CsvDatasetRepository;

/**
 * Coordinates the landing screen and switches between GUI sections.
 */
public final class AppShellController {
    private final AppShellView view;
    private final DashboardController dataAnalysisController;
    private final GradePredictionController gradePredictionController;

    public AppShellController() throws Exception {
        AppData appData = new CsvDatasetRepository().load();
        this.view = new AppShellView();
        this.dataAnalysisController = new DashboardController(appData);
        this.gradePredictionController = new GradePredictionController(appData);
        bind();
        view.showHome();
    }

    public Parent root() {
        return view;
    }

    private void bind() {
        view.dataAnalysisButton().setOnAction(event -> view.showSection("Data Analysis", dataAnalysisController.root()));
        view.gradePredictionButton().setOnAction(event -> view.showSection("Grade Prediction", gradePredictionController.root()));
        view.backButton().setOnAction(event -> view.showHome());
    }
}
