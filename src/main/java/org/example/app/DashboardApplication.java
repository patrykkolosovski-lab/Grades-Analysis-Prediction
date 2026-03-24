package org.example.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.ui.AppShellController;

/**
 * Starts the refactored JavaFX dashboard application.
 */
public final class DashboardApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        AppShellController controller = new AppShellController();
        Scene scene = new Scene(controller.root(), 1360, 900);
        scene.getStylesheets().add(
                DashboardApplication.class.getResource("/styles/app.css").toExternalForm()
        );
        stage.setTitle("Grade Dashboard");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
