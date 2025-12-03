package org.example.GUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class startClass extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GradeDashboardApp app = new GradeDashboardApp();
        BorderPane root = new BorderPane();

        // Left: controls, Center: charts, Bottom: status
        root.setLeft(app.createControlPanel());
        root.setCenter(app.createDashboardArea());
        root.setBottom(app.createStatusBar());

        // Load CSVs using your dataCollector
        app.initDatasets();

        // Populate course list once (union of all courses)
        app.populateCourseList();

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Phase 2 Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
