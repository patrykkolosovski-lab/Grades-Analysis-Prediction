package org.example;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.*;

/***
 * For this class to run you need to:
 * - build a project in Maven
 * - put all java files into org.example folder
 * - put the csv files into resources folder
 * - modify the pom.xml so that it states this class as main
 * - incorporate the plugin and dependencies of javaFx in the pom.xml
 */

public class GradeDashboardApp extends Application {

    private static final String PATH_GRAD = "src/main/resources/GraduateGrades.csv";
    private static final String PATH_CURR = "src/main/resources/CurrentGrades.csv";
    private static final String StudentInformation = "src/main/resources/StudentInfo.csv";

    // What the user sees in the dataset combo
    private static final String DS_GRAD_DISPLAY = "resources/GraduateGrades.csv";
    private static final String DS_CURR_DISPLAY = "resources/CurrentGrades.csv";
    private static final String DS_BOTH_DISPLAY = "Both (merged)";

    // Filter feature names (first scroll)
    private static final String FEATURE_GRADE_THRESHOLD = "Grade threshold";
    private static final String FEATURE_PSIONIC = "Psionic Interference Tolerance";
    private static final String FEATURE_QCT = "Quantum Coherence Threshold";
    private static final String FEATURE_SYMBIOTIC = "Symbiotic Network Compatibility";
    private static final String FEATURE_ATDR = "Astro-Temporal Drift Resistance";
    private static final String FEATURE_BLT = "Bio-Luminal Transmission";

    // All feature options for the first scroll
    private static final String[] FILTER_FEATURES = {
            FEATURE_GRADE_THRESHOLD,
            FEATURE_PSIONIC,
            FEATURE_QCT,
            FEATURE_SYMBIOTIC,
            FEATURE_ATDR,
            FEATURE_BLT
    };

    // Possible categorical values per feature (for second scroll)
    private static final Map<String, List<String>> CATEGORICAL_VALUES = new LinkedHashMap<>();
    static {
        CATEGORICAL_VALUES.put(FEATURE_QCT, Arrays.asList(
                "Stable", "Fractured", "Chaotic", "Coherent", "Resonant"
        ));
        CATEGORICAL_VALUES.put(FEATURE_SYMBIOTIC, Arrays.asList(
                "None", "Harmonized"
        ));
        CATEGORICAL_VALUES.put(FEATURE_ATDR, Arrays.asList(
                "1 ns/mc", "2 ns/mc", "3 ns/mc"
        ));
        CATEGORICAL_VALUES.put(FEATURE_BLT, Arrays.asList(
                "Silver", "Crimson", "White-Blue", "Violet"
        ));
    }

    // Raw data arrays (row = student, col = course)
    private String[][] graduateGrades;
    private String[][] currentGrades;
    private String[][] mergedGrades;
    private String[][] studentInformation;

    // Course name arrays (index = courseColumn)
    private String[] graduateCourseNames;
    private String[] currentCourseNames;
    private String[] mergedCourseNames;

    // UI controls
    private ComboBox<String> datasetComboBox;
    private Spinner<Integer> courseCountSpinner;
    private ListView<String> courseListView;

    // Filtering
    private VBox filterContainer; // holds many filter rows
    private ComboBox<String> categoricalValueCombo;
    ComboBox<String> featureCombo;

    // Visualisation
    private ComboBox<PlotType> plotTypeComboBox;
    private VisualizationPane[] courseViews; // up to 4 panes
    private GridPane dashboardGrid;

    private Label statusLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // Left: controls, Center: charts, Bottom: status
        root.setLeft(createControlPanel());
        root.setCenter(createDashboardArea());
        root.setBottom(createStatusBar());

        // Load CSVs using your dataCollector
        initDatasets();

        // Populate course list once (union of all courses)
        populateCourseList();

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Phase 2 Dashboard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // ---------------------------------------------------------------------
    // Left control panel (Data selection + Filtering + Visualisation)
    // ---------------------------------------------------------------------
    private Node createControlPanel() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(10));
        root.setPrefWidth(360);

        TitledPane dataSelectionPane = new TitledPane("1. Data selection", createDataSelectionBox());
        dataSelectionPane.setCollapsible(false);

        TitledPane filterPane = new TitledPane("2. Filtering", createFilterBox());
        filterPane.setCollapsible(false);

        TitledPane vizPane = new TitledPane("3. Visualisation", createVisualisationBox());
        vizPane.setCollapsible(false);

        root.getChildren().addAll(dataSelectionPane, filterPane, vizPane);

        ScrollPane scroll = new ScrollPane(root);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scroll;
    }

    // ---------------- Data selection: number of courses + multi-select list ----------
    private VBox createDataSelectionBox() {
        VBox box = new VBox(8);
        box.setPadding(new Insets(8));

        Label datasetLabel = new Label("Dataset:");
        datasetComboBox = new ComboBox<>();
        datasetComboBox.setItems(FXCollections.observableArrayList(
                DS_GRAD_DISPLAY,
                DS_CURR_DISPLAY,
                DS_BOTH_DISPLAY
        ));
        datasetComboBox.getSelectionModel().selectFirst();

        Label howManyLabel = new Label("How many courses to compare (1–4):");
        courseCountSpinner = new Spinner<>(1, 4, 1);
        courseCountSpinner.setEditable(false);
        courseCountSpinner.setMaxWidth(Double.MAX_VALUE);

        Label coursesLabel = new Label("Select the course(s):");
        courseListView = new ListView<>();
        courseListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        courseListView.setPrefHeight(150);

        box.getChildren().addAll(
                datasetLabel,
                datasetComboBox,
                new Separator(),
                howManyLabel,
                courseCountSpinner,
                new Separator(),
                coursesLabel,
                courseListView
        );
        return box;
    }

    // ---------------- Filtering: single feature scroll + dynamic controls --------
    private VBox createFilterBox() {
        VBox box = new VBox(8);
        box.setPadding(new Insets(8));

        Label info = new Label("You can add multiple filters:\n");
        info.setWrapText(true);

        filterContainer = new VBox(5);
        filterContainer.setFillWidth(true);

        // Start with one empty filter row
        filterContainer.getChildren().add(createFilterRow());

        Button addFilterButton = new Button("Add filter");
        addFilterButton.setOnAction(e -> filterContainer.getChildren().add(createFilterRow()));

        box.getChildren().addAll(info, filterContainer, addFilterButton);
        return box;
    }


    private HBox createFilterRow() {
        HBox row = new HBox(5);
        row.setAlignment(Pos.CENTER_LEFT);

        // 1) Feature selection (grade threshold + all student features)
        featureCombo = new ComboBox<>();
        featureCombo.setItems(FXCollections.observableArrayList(FILTER_FEATURES));
        featureCombo.setPromptText("Select feature");

        // 2) Operator (for grade threshold and psionic)
        ComboBox<String> operatorCombo = new ComboBox<>();
        operatorCombo.setItems(FXCollections.observableArrayList(">", ">=", "<", "<=", "="));
        operatorCombo.getSelectionModel().select(">");

        // 3) Numeric value field (grade threshold or Psionic Interference Tolerance)
        TextField valueField = new TextField();

        // 4) Categorical value combo (for QCT, Symbiotic, ATDR, BLT)
        categoricalValueCombo = new ComboBox<>();
        categoricalValueCombo.setPromptText("Category value");

        // Initial visibility: hide all dynamic controls until feature is chosen
        operatorCombo.setVisible(false);
        operatorCombo.setManaged(false);
        valueField.setVisible(false);
        valueField.setManaged(false);
        categoricalValueCombo.setVisible(false);
        categoricalValueCombo.setManaged(false);

        // React to feature selection
        featureCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) {
                operatorCombo.setVisible(false);
                operatorCombo.setManaged(false);
                valueField.setVisible(false);
                valueField.setManaged(false);
                categoricalValueCombo.setVisible(false);
                categoricalValueCombo.setManaged(false);
                categoricalValueCombo.getItems().clear();
                return;
            }

            if (newVal.equals(FEATURE_GRADE_THRESHOLD)) {
                // Grade threshold → operator + numeric value
                operatorCombo.setVisible(true);
                operatorCombo.setManaged(true);

                valueField.setVisible(true);
                valueField.setManaged(true);
                valueField.setPromptText("grade threshold (e.g. 7.5)");

                categoricalValueCombo.setVisible(false);
                categoricalValueCombo.setManaged(false);
                categoricalValueCombo.getItems().clear();

            } else if (newVal.equals(FEATURE_PSIONIC)) {
                // Psionic Interference Tolerance → operator + numeric value (same logic as grade threshold)
                operatorCombo.setVisible(true);
                operatorCombo.setManaged(true);

                valueField.setVisible(true);
                valueField.setManaged(true);
                valueField.setPromptText("psionic value (e.g. 0.5)");

                categoricalValueCombo.setVisible(false);
                categoricalValueCombo.setManaged(false);
                categoricalValueCombo.getItems().clear();

            } else {
                // Other features → categorical value scroll
                operatorCombo.setVisible(false);
                operatorCombo.setManaged(false);

                valueField.setVisible(false);
                valueField.setManaged(false);


                // TODO: POINT OF INTEREST
                List<String> cats = CATEGORICAL_VALUES.getOrDefault(newVal, Collections.emptyList());
                categoricalValueCombo.setItems(FXCollections.observableArrayList(cats));
                if (!cats.isEmpty()) {
                    categoricalValueCombo.getSelectionModel().selectFirst();
                }

                categoricalValueCombo.setVisible(true);
                categoricalValueCombo.setManaged(true);
            }
        });

        // Delete row button
        Button deleteButton = new Button("x");
        deleteButton.setOnAction(e -> {
            VBox parent = (VBox) row.getParent();
            parent.getChildren().remove(row);
        });

        HBox.setHgrow(valueField, Priority.ALWAYS);
        HBox.setHgrow(featureCombo, Priority.SOMETIMES);
        HBox.setHgrow(categoricalValueCombo, Priority.ALWAYS);

        // Store row meta in userData so we can read all filters later (if needed)
        FilterRow meta = new FilterRow(featureCombo, operatorCombo, valueField, categoricalValueCombo);
        row.setUserData(meta);

        row.getChildren().addAll(
                featureCombo,
                operatorCombo,
                valueField,
                categoricalValueCombo,
                deleteButton
        );
        return row;
    }

    // ---------------- Visualisation: list all plot types ------------------
    private VBox createVisualisationBox() {
        VBox box = new VBox(8);
        box.setPadding(new Insets(8));


        Label plotTypeLabel = new Label("Plot type:");
        plotTypeComboBox = new ComboBox<>();
        plotTypeComboBox.setItems(FXCollections.observableArrayList(
                PlotType.BAR,
                PlotType.HISTOGRAM,
                PlotType.SCATTER,
                PlotType.SWARM,
                PlotType.JOINT
        ));
        plotTypeComboBox.getSelectionModel().select(PlotType.HISTOGRAM);

        Button renderButton = new Button("Render visualisation");
        renderButton.setMaxWidth(Double.MAX_VALUE);
        renderButton.setOnAction(e -> onRenderVisualization());

        box.getChildren().addAll(
                plotTypeLabel, plotTypeComboBox,
                new Separator(),
                renderButton
        );
        return box;
    }

    // ---------------------------------------------------------------------
    // Center dashboard area – grid for up to 4 courses
    // ---------------------------------------------------------------------
    private Node createDashboardArea() {
        dashboardGrid = new GridPane();
        dashboardGrid.setPadding(new Insets(10));
        dashboardGrid.setHgap(10);
        dashboardGrid.setVgap(10);

        courseViews = new VisualizationPane[4];
        for (int i = 0; i < 4; i++) {
            courseViews[i] = new VisualizationPane("Course " + (i + 1));
            int row = i / 2;
            int col = i % 2;
            dashboardGrid.add(courseViews[i], col, row);
        }
        return dashboardGrid;
    }

    // ---------------------------------------------------------------------
    // Status bar
    // ---------------------------------------------------------------------
    private HBox createStatusBar() {
        HBox statusBar = new HBox();
        statusBar.setPadding(new Insets(4, 8, 4, 8));
        statusBar.setSpacing(8);
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setStyle("-fx-background-color: #f0f0f0;");

        statusLabel = new Label("Ready.");
        statusBar.getChildren().add(statusLabel);
        return statusBar;
    }

    private void setStatus(String text) {
        if (statusLabel != null) {
            statusLabel.setText(text);
        }
    }

    // Dataset loading (uses dataCollector)
    private void initDatasets() {
        try {
            dataCollector gradCollector = new dataCollector(PATH_GRAD);
            graduateGrades = gradCollector.GraduateGradesArray;
            graduateCourseNames = gradCollector.CourseNamesArray;

            dataCollector currCollector = new dataCollector(PATH_CURR);
            currentGrades = currCollector.GraduateGradesArray; // same layout
            currentCourseNames = currCollector.CourseNamesArray;

            StudentInformationReader studentinformationreader = new StudentInformationReader(StudentInformation);
            studentInformation = studentinformationreader.StudentInfoArray;

            // merged: rows from both, union of course names
            if (graduateGrades != null && currentGrades != null) {
                int rows = graduateGrades.length + currentGrades.length;
                int cols = graduateGrades[0].length;
                mergedGrades = new String[rows][cols];

                for (int i = 0; i < graduateGrades.length; i++) {
                    mergedGrades[i] = Arrays.copyOf(graduateGrades[i], cols);
                }
                for (int i = 0; i < currentGrades.length; i++) {
                    mergedGrades[graduateGrades.length + i] = Arrays.copyOf(currentGrades[i], cols);
                }

                Set<String> union = new LinkedHashSet<>();
                union.addAll(Arrays.asList(graduateCourseNames));
                union.addAll(Arrays.asList(currentCourseNames));
                mergedCourseNames = union.toArray(new String[0]);
            }

            setStatus("Loaded datasets successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            setStatus("Error loading datasets: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------------
    // Course list = union of all courses (no dataset filtering)
    // ---------------------------------------------------------------------
    private void populateCourseList() {
        String[] courseNames = (mergedCourseNames != null)
                ? mergedCourseNames
                : graduateCourseNames;

        List<String> filtered = new ArrayList<>();
        if (courseNames != null) {
            for (String c : courseNames) {
                if (c != null && !c.trim().isEmpty()) {
                    filtered.add(c.trim());
                }
            }
        }

        ObservableList<String> courseList = FXCollections.observableArrayList(filtered);

        courseListView.setItems(courseList);
        courseListView.getSelectionModel().clearSelection();

        setStatus("Courses loaded (" + courseList.size() + " total).");
    }

    // ---------------------------------------------------------------------
    // Render logic – all plot types with required validations
    // ---------------------------------------------------------------------
    private void onRenderVisualization() {
        String datasetDisplay = datasetComboBox.getValue();
        PlotType plotType = plotTypeComboBox.getValue();

        String chosenFeatureType = featureCombo.getValue();
        String chosenFeature = categoricalValueCombo.getValue();
        // System.out.println(featureCombo.getValue());
        // System.out.println(categoricalValueCombo.getValue());

        int maxCourses = courseCountSpinner.getValue();
        ObservableList<String> selected = courseListView.getSelectionModel().getSelectedItems();

        List<String> chosenCourses = new ArrayList<>();
        if (selected != null) {
            chosenCourses.addAll(selected);
        }

        if (chosenCourses.isEmpty()) {
            setStatus("Please select at least one course.");
            for (VisualizationPane vp : courseViews) {
                vp.setTitle("No course");
                vp.setPlaceholderText("No course selected.");
                vp.setVisible(false);
                vp.setManaged(false);
            }
            return;
        }

        if (chosenCourses.size() > maxCourses) {
            chosenCourses = chosenCourses.subList(0, maxCourses);
        }

        // choose data arrays
        String[][] gradesData;
        String[] courseNames;
        if (DS_GRAD_DISPLAY.equals(datasetDisplay)) {
            gradesData = graduateGrades;
            courseNames = graduateCourseNames;
        } else if (DS_CURR_DISPLAY.equals(datasetDisplay)) {
            gradesData = currentGrades;
            courseNames = currentCourseNames;
        } else {
            gradesData = mergedGrades;
            courseNames = mergedCourseNames;
        }

        if (gradesData == null || courseNames == null) {
            setStatus("Dataset not loaded correctly.");
            return;
        }

        switch (plotType) {
            case BAR:
                handleBarPlot(datasetDisplay, chosenCourses, gradesData, courseNames, chosenFeature);
                break;
            case HISTOGRAM:
                handleHistogramPlot(datasetDisplay, chosenCourses, gradesData, courseNames, chosenFeature);
                break;
            case SCATTER:
                handleScatterPlot(datasetDisplay, chosenCourses, gradesData, courseNames);
                break;
            case SWARM:
                handleSwarmPlot(datasetDisplay, chosenCourses, gradesData, courseNames, chosenFeature);
                break;
            case JOINT:
                handleJointPlot(datasetDisplay, chosenCourses, gradesData, courseNames);
                break;
        }
    }

    // ---------------------------------------------------------------------
    // BAR: 1–4 courses in a single multi-series bar chart
    // ---------------------------------------------------------------------
    private void handleBarPlot(String datasetDisplay,
                               List<String> chosenCourses,
                               String[][] gradesData,
                               String[] courseNames,
                               String feature) {

        Map<String, double[]> courseGradesMap = buildCourseGradesMap(chosenCourses, gradesData, courseNames, feature);

        if (courseGradesMap.isEmpty()) {
            for (VisualizationPane vp : courseViews) {
                vp.setPlaceholderText("No valid grades for selected courses.");
                vp.setVisible(false);
                vp.setManaged(false);
            }
            setStatus("No valid grades for selected courses.");
            return;
        }

        BarChart<String, Number> chart =
                createMultiCourseBarChart(courseGradesMap, "Grade distribution – " + datasetDisplay);

        courseViews[0].setTitle("Bar chart – selected courses");
        courseViews[0].setContent(chart);
        courseViews[0].setVisible(true);
        courseViews[0].setManaged(true);

        for (int i = 1; i < courseViews.length; i++) {
            courseViews[i].setVisible(false);
            courseViews[i].setManaged(false);
        }

        setStatus("Rendered BAR chart for " + courseGradesMap.keySet() + " [" + datasetDisplay + "]");
    }

    // ---------------------------------------------------------------------
    // HISTOGRAM: 1–4 separate histograms (same logic as before)
    // ---------------------------------------------------------------------
    private void handleHistogramPlot(String datasetDisplay,
                                     List<String> chosenCourses,
                                     String[][] gradesData,
                                     String[] courseNames,
                                     String chosenFeature) {

        Map<String, double[]> courseGradesMap = buildCourseGradesMap(chosenCourses, gradesData, courseNames, chosenFeature);

        if (courseGradesMap.isEmpty()) {
            for (VisualizationPane vp : courseViews) {
                vp.setPlaceholderText("No valid grades for selected courses.");
                vp.setVisible(false);
                vp.setManaged(false);
            }
            setStatus("No valid grades for selected courses.");
            return;
        }

        int paneIndex = 0;
        for (Map.Entry<String, double[]> entry : courseGradesMap.entrySet()) {
            if (paneIndex >= courseViews.length) break;
            String courseName = entry.getKey();
            double[] grades = entry.getValue();

            if (grades.length == 0) {
                courseViews[paneIndex].setTitle(courseName);
                courseViews[paneIndex].setPlaceholderText("No valid grades for course: " + courseName);
            } else {
                BarChart<String, Number> hist = createHistogramChart(
                        grades,
                        "Grade distribution – " + courseName
                );
                courseViews[paneIndex].setTitle(courseName);
                courseViews[paneIndex].setContent(hist);
            }

            courseViews[paneIndex].setVisible(true);
            courseViews[paneIndex].setManaged(true);
            paneIndex++;
        }

        for (int i = paneIndex; i < courseViews.length; i++) {
            courseViews[i].setVisible(false);
            courseViews[i].setManaged(false);
        }

        setStatus("Rendered HISTOGRAM for " + courseGradesMap.keySet() + " [" + datasetDisplay + "]");
    }

    // SCATTER: must choose EXACTLY 2 courses
    private void handleScatterPlot(String datasetDisplay,
                                   List<String> chosenCourses,
                                   String[][] gradesData,
                                   String[] courseNames) {

        if (chosenCourses.size() != 2) {
            courseViews[0].setTitle("Scatter plot");
            courseViews[0].setPlaceholderText(
                    "Scatter plot requires exactly 2 courses.\n" +
                            "Currently selected: " + chosenCourses.size() + "."
            );
            courseViews[0].setVisible(true);
            courseViews[0].setManaged(true);

            for (int i = 1; i < courseViews.length; i++) {
                courseViews[i].setVisible(false);
                courseViews[i].setManaged(false);
            }

            setStatus("Scatter plot requires exactly 2 courses; currently selected: " + chosenCourses.size());
            return;
        }

        String courseX = chosenCourses.get(0);
        String courseY = chosenCourses.get(1);

        int colX = findCourseColumn(courseNames, courseX);
        int colY = findCourseColumn(courseNames, courseY);

        if (colX < 0 || colY < 0) {
            setStatus("Could not find course columns for scatter plot.");
            return;
        }

        double[][] xy = collectPairedGrades(gradesData, colX, colY);
        double[] xs = xy[0];
        double[] ys = xy[1];

        if (xs.length == 0) {
            courseViews[0].setTitle("Scatter plot: " + courseX + " vs " + courseY);
            courseViews[0].setPlaceholderText("No paired grades available for selected courses.");
            courseViews[0].setVisible(true);
            courseViews[0].setManaged(true);

            for (int i = 1; i < courseViews.length; i++) {
                courseViews[i].setVisible(false);
                courseViews[i].setManaged(false);
            }

            setStatus("No paired grades for scatter plot.");
            return;
        }

        ScatterChart<Number, Number> scatterChart =
                createScatterChart(xs, ys, courseX, courseY);

        courseViews[0].setTitle("Scatter plot: " + courseX + " vs " + courseY);
        courseViews[0].setContent(scatterChart);
        courseViews[0].setVisible(true);
        courseViews[0].setManaged(true);

        for (int i = 1; i < courseViews.length; i++) {
            courseViews[i].setVisible(false);
            courseViews[i].setManaged(false);
        }

        setStatus("Rendered SCATTER plot for [" + courseX + ", " + courseY + "] in " + datasetDisplay);
    }

    // SWARM: 1–4 courses in one swarm-like scatter plot
    private void handleSwarmPlot(String datasetDisplay,
                                 List<String> chosenCourses,
                                 String[][] gradesData,
                                 String[] courseNames,
                                 String chosenFeature) {

        Map<String, double[]> courseGradesMap = buildCourseGradesMap(chosenCourses, gradesData, courseNames, chosenFeature);

        if (courseGradesMap.isEmpty()) {
            courseViews[0].setTitle("Swarm plot");
            courseViews[0].setPlaceholderText("No valid grades for selected courses.");
            courseViews[0].setVisible(true);
            courseViews[0].setManaged(true);

            for (int i = 1; i < courseViews.length; i++) {
                courseViews[i].setVisible(false);
                courseViews[i].setManaged(false);
            }

            setStatus("No valid grades for swarm plot.");
            return;
        }

        ScatterChart<Number, Number> swarmChart =
                createSwarmChart(courseGradesMap, "Swarm plot – " + datasetDisplay);

        courseViews[0].setTitle("Swarm plot – selected courses");
        courseViews[0].setContent(swarmChart);
        courseViews[0].setVisible(true);
        courseViews[0].setManaged(true);

        for (int i = 1; i < courseViews.length; i++) {
            courseViews[i].setVisible(false);
            courseViews[i].setManaged(false);
        }

        setStatus("Rendered SWARM plot for " + courseGradesMap.keySet() + " [" + datasetDisplay + "]");
    }

    // JOINT: must choose EXACTLY 2 courses (currently placeholder)
    private void handleJointPlot(String datasetDisplay,
                                 List<String> chosenCourses,
                                 String[][] gradesData,
                                 String[] courseNames) {

        if (chosenCourses.size() != 2) {
            courseViews[0].setTitle("Joint plot");
            courseViews[0].setPlaceholderText(
                    "Joint plot requires exactly 2 courses.\n" +
                            "Currently selected: " + chosenCourses.size() + "."
            );
            courseViews[0].setVisible(true);
            courseViews[0].setManaged(true);

            for (int i = 1; i < courseViews.length; i++) {
                courseViews[i].setVisible(false);
                courseViews[i].setManaged(false);
            }

            setStatus("Joint plot requires exactly 2 courses; currently selected: " + chosenCourses.size());
            return;
        }

        String courseA = chosenCourses.get(0);
        String courseB = chosenCourses.get(1);

        courseViews[0].setTitle("Joint plot: " + courseA + " vs " + courseB);
        courseViews[0].setPlaceholderText(
                "Joint plot for courses \"" + courseA + "\" and \"" + courseB + "\"\n" +
                        "is not implemented yet."
        );
        courseViews[0].setVisible(true);
        courseViews[0].setManaged(true);

        for (int i = 1; i < courseViews.length; i++) {
            courseViews[i].setVisible(false);
            courseViews[i].setManaged(false);
        }

        setStatus("Joint plot: validation passed (2 courses selected), but rendering not implemented.");
    }

    // ---------------------------------------------------------------------
    // Helpers for building course → grades map
    // ---------------------------------------------------------------------
    private Map<String, double[]> buildCourseGradesMap(List<String> chosenCourses,
                                                       String[][] gradesData,
                                                       String[] courseNames,
                                                       String chosenFeature) {
        Map<String, double[]> courseGradesMap = new LinkedHashMap<>();
        for (String courseName : chosenCourses) {
            int courseColumn = findCourseColumn(courseNames, courseName);
            if (courseColumn < 0) {
                continue;
            }
            phase2Handler handler;
            try {
                System.err.println("Try block started");
                handler = new phase2Handler(gradesData, studentInformation, courseColumn);
                System.err.println("phase2Handler made");
                handler.setChosenDataWithDataFeature(chosenFeature);
                System.err.println("chosenFeature set");
            } catch(Exception e) {
                System.out.println(e.getMessage());
                handler = new phase2Handler(gradesData, null, courseColumn);
                handler.setChosenData();
            }


            String[] chosenData = handler.chosenData;
            double[] allGrades = parseGrades(chosenData);
            if (allGrades.length > 0) {
                courseGradesMap.put(courseName, allGrades);
            }
        }
        return courseGradesMap;
    }

    // ---------------------------------------------------------------------
    // Paired grades for scatter/joint
    // ---------------------------------------------------------------------
    private double[][] collectPairedGrades(String[][] data, int colX, int colY) {
        List<Double> xs = new ArrayList<>();
        List<Double> ys = new ArrayList<>();

        for (String[] row : data) {
            if (row == null) continue;
            String sx = (colX >= 0 && colX < row.length) ? row[colX] : null;
            String sy = (colY >= 0 && colY < row.length) ? row[colY] : null;

            if (sx == null || sy == null) continue;
            if (sx.equalsIgnoreCase("NG") || sy.equalsIgnoreCase("NG")) continue;

            try {
                double dx = Double.parseDouble(sx);
                double dy = Double.parseDouble(sy);
                xs.add(dx);
                ys.add(dy);
            } catch (NumberFormatException ignored) {
            }
        }

        double[] xArr = new double[xs.size()];
        double[] yArr = new double[ys.size()];
        for (int i = 0; i < xs.size(); i++) {
            xArr[i] = xs.get(i);
            yArr[i] = ys.get(i);
        }
        return new double[][]{xArr, yArr};
    }

    // ---------------------------------------------------------------------
    // Parsing and simple helpers
    // ---------------------------------------------------------------------
    private int findCourseColumn(String[] courseNames, String courseName) {
        if (courseNames == null) return -1;
        for (int i = 0; i < courseNames.length; i++) {
            if (courseNames[i] != null && courseNames[i].equalsIgnoreCase(courseName)) {
                return i;
            }
        }
        return -1;
    }

    private double[] parseGrades(String[] grades) {
        if (grades == null) return new double[0];
        double[] tmp = new double[grades.length];
        int n = 0;
        for (String g : grades) {
            if (g == null) continue;
            if (g.equalsIgnoreCase("NG")) continue;
            try {
                tmp[n++] = Double.parseDouble(g);
            } catch (NumberFormatException ignored) {
            }
        }
        return Arrays.copyOf(tmp, n);
    }

    // ---------------------------------------------------------------------
    // Histogram helpers – axes correctly labelled
    // ---------------------------------------------------------------------
    private BarChart<String, Number> createHistogramChart(double[] grades, String title) {
        String[] labels = {
                "5",
                "6",
                "7",
                "8",
                "9",
                "10"
        };
        int[] counts = new int[labels.length];

        for (double g : grades) {
            if (g >= 5.0 && g < 6.0) {
                counts[0]++;
            } else if (g >= 6.0 && g < 7.0) {
                counts[1]++;
            } else if (g >= 7.0 && g < 8.0) {
                counts[2]++;
            } else if (g >= 8.0 && g < 9.0) {
                counts[3]++;
            } else if (g >= 9.0 && g < 10.0) {
                counts[4]++;
            } else if (g == 10.0){
                counts[5]++;
            }
        }

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Grade range");
        xAxis.setCategories(FXCollections.observableArrayList(labels));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of students");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < labels.length; i++) {
            if (counts[i] > 0) {
                series.getData().add(new XYChart.Data<>(labels[i], counts[i]));
            }
        }
        chart.getData().add(series);
        return chart;
    }

    // Multi-course bar chart: each course is a series
    private BarChart<String, Number> createMultiCourseBarChart(Map<String, double[]> courseGradesMap,
                                                               String title) {
        String[] labels = {
                "5",
                "6",
                "7",
                "8",
                "9",
                "10"
        };

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Grade range");
        xAxis.setCategories(FXCollections.observableArrayList(labels));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of students");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setLegendVisible(true);

        for (Map.Entry<String, double[]> entry : courseGradesMap.entrySet()) {
            String courseName = entry.getKey();
            double[] grades = entry.getValue();

            int[] counts = new int[labels.length];
            for (double g : grades) {
                if (g >= 5.0 && g < 6.0) {
                    counts[0]++;
                } else if (g >= 6.0 && g < 7.0) {
                    counts[1]++;
                } else if (g >= 7.0 && g < 8.0) {
                    counts[2]++;
                } else if (g >= 8.0 && g < 9.0) {
                    counts[3]++;
                } else if (g >= 9.0 && g <= 10.0) {
                    counts[4]++;
                } else if (g == 10.0) {
                    counts[5]++;
                }
            }

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(courseName);
            for (int i = 0; i < labels.length; i++) {
                if (counts[i] > 0) {
                    series.getData().add(new XYChart.Data<>(labels[i], counts[i]));
                }
            }
            chart.getData().add(series);
        }

        return chart;
    }

    // ---------------------------------------------------------------------
    // Scatter plot helper
    // ---------------------------------------------------------------------
    private ScatterChart<Number, Number> createScatterChart(double[] xs,
                                                            double[] ys,
                                                            String courseX,
                                                            String courseY) {
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel(courseX);

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(courseY);

        ScatterChart<Number, Number> chart = new ScatterChart<>(xAxis, yAxis);
        chart.setTitle("Scatter: " + courseX + " vs " + courseY);

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Students");

        for (int i = 0; i < xs.length; i++) {
            series.getData().add(new XYChart.Data<>(xs[i], ys[i]));
        }

        chart.getData().add(series);
        return chart;
    }

    // ---------------------------------------------------------------------
    // Swarm plot helper (ScatterChart with jittered x for each course)
    // ---------------------------------------------------------------------
    private ScatterChart<Number, Number> createSwarmChart(Map<String, double[]> courseGradesMap,
                                                          String title) {
        List<String> courseNames = new ArrayList<>(courseGradesMap.keySet());
        int n = courseNames.size();

        // X-axis: numeric positions 1..n with labels mapped to course names
        NumberAxis xAxis = new NumberAxis(0.5, n + 0.5, 1.0);
        xAxis.setLabel("Course");
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number object) {
                int idx = object.intValue() - 1;
                if (idx >= 0 && idx < courseNames.size()) {
                    return courseNames.get(idx);
                }
                return "";
            }

            @Override
            public Number fromString(String string) {
                return 0; // not used
            }
        });

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Grade");

        ScatterChart<Number, Number> chart = new ScatterChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setLegendVisible(true);

        Random random = new Random();

        for (int i = 0; i < courseNames.size(); i++) {
            String courseName = courseNames.get(i);
            double[] grades = courseGradesMap.get(courseName);
            if (grades == null || grades.length == 0) continue;

            XYChart.Series<Number, Number> series = new XYChart.Series<>();
            series.setName(courseName);

            double xCenter = i + 1; // positions: 1,2,3,4
            for (double g : grades) {
                double jitter = (random.nextDouble() - 0.5) * 0.4; // spread around center
                double x = xCenter + jitter;
                series.getData().add(new XYChart.Data<>(x, g));
            }

            chart.getData().add(series);
        }

        return chart;
    }

    // ---------------------------------------------------------------------
    // Helper UI classes & enums
    // ---------------------------------------------------------------------
    private static class VisualizationPane extends BorderPane {
        private final Label placeholderLabel;
        private final Label titleLabel;

        public VisualizationPane(String title) {
            setPadding(new Insets(10));

            titleLabel = new Label(title);
            titleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            setTop(titleLabel);
            BorderPane.setMargin(titleLabel, new Insets(0, 0, 8, 0));

            placeholderLabel = new Label("No visualisation yet.");
            placeholderLabel.setWrapText(true);
            StackPane center = new StackPane(placeholderLabel);
            center.setStyle("-fx-border-color: #cccccc; -fx-border-width: 1; -fx-background-color: #fcfcfc;");
            setCenter(center);
        }

        public void setPlaceholderText(String text) {
            placeholderLabel.setText(text);
            setCenter(new StackPane(placeholderLabel));
        }

        public void setContent(Node node) {
            if (node == null) {
                setCenter(new StackPane(placeholderLabel));
            } else {
                setCenter(node);
            }
        }

        public void setTitle(String title) {
            titleLabel.setText(title);
        }
    }

    // Plot types from Phase 2 handbook – all listed in the ComboBox
    private enum PlotType {
        BAR("Bar chart"),
        HISTOGRAM("Histogram"),
        SCATTER("Scatter plot"),
        SWARM("Swarm plot"),
        JOINT("Joint plot");

        private final String label;

        PlotType(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    // Small helper class to keep filter-row controls together
    private static class FilterRow {
        ComboBox<String> featureCombo;
        ComboBox<String> operatorCombo;
        TextField valueField;
        ComboBox<String> categoricalValueCombo;

        FilterRow(ComboBox<String> featureCombo,
                  ComboBox<String> operatorCombo,
                  TextField valueField,
                  ComboBox<String> categoricalValueCombo) {
            this.featureCombo = featureCombo;
            this.operatorCombo = operatorCombo;
            this.valueField = valueField;
            this.categoricalValueCombo = categoricalValueCombo;
        }
    }
}