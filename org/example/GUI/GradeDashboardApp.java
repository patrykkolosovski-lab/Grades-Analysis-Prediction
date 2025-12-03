package org.example.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.example.collectors.StudentInformationReader;
import org.example.collectors.dataCollector;
import javafx.scene.shape.Rectangle;
import org.example.phase2Handler;

import java.util.*;


public class GradeDashboardApp {

    private static final String PATH_GRAD = filePaths.getPathGrad();
    private static final String PATH_CURR = filePaths.getPathCurr();
    private static final String StudentInformation = filePaths.getStudentInformation();

    // What the user sees in the dataset combo
    private static final String DS_GRAD_DISPLAY = "GraduateGrades.csv";
    private static final String DS_CURR_DISPLAY = "CurrentGrades.csv";
    private static final String DS_BOTH_DISPLAY = "Both (merged)";

    // Filter feature names (first scroll)
    private static final String FEATURE_PSIONIC = featureInfo.getFeaturePsionic();
    private static final String FEATURE_NG = featureInfo.getFeatureNg();

    // All feature options for the first scroll
    private static final String[] FILTER_FEATURES = featureInfo.getFilterFeatures();

    // Possible categorical values per feature (for second scroll)
    private static final Map<String, List<String>> CATEGORICAL_VALUES = featureInfo.getCategoricalValues();

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
    private ComboBox<String> categoricalValueCombo;
    private ComboBox<String> featureCombo;
    private ComboBox<String> operatorCombo;
    private TextField valueField;
    private CheckBox splitByFeatureCheckBox;


    // Visualisation
    private ComboBox<PlotType> plotTypeComboBox;
    private VisualizationPane[] courseViews; // up to 4 panes
    private GridPane dashboardGrid;

    private Label statusLabel;

    // Left control panel (Data selection + Filtering + Visualisation)
    Node createControlPanel() {
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

    // Data selection stuff: number of courses + multi-select list
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

        Label info = new Label("Filter the data:\n");
        info.setWrapText(true);

        HBox filterRow = createFilterRow();
        splitByFeatureCheckBox = new CheckBox("Show filtered vs all students");
        box.getChildren().addAll(info, filterRow, splitByFeatureCheckBox);

        return box;
    }


    private HBox createFilterRow() {
        HBox row = new HBox(5);
        row.setAlignment(Pos.CENTER_LEFT);

        // 1) Feature selection (Psionic + all student features + NG)
        featureCombo = new ComboBox<>();
        featureCombo.setItems(FXCollections.observableArrayList(FILTER_FEATURES));
        featureCombo.setPromptText("Select feature");

        // 2) Operator (for psionic numeric filtering)
        operatorCombo = new ComboBox<>();
        operatorCombo.setItems(FXCollections.observableArrayList(">", ">=", "<", "<=", "="));
        operatorCombo.getSelectionModel().select(">");

        // 3) Numeric value field (for Psionic Interference Tolerance)
        valueField = new TextField();

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
            // reset all secondary controls
            operatorCombo.setVisible(false);
            operatorCombo.setManaged(false);
            valueField.setVisible(false);
            valueField.setManaged(false);
            valueField.clear();
            categoricalValueCombo.setVisible(false);
            categoricalValueCombo.setManaged(false);
            categoricalValueCombo.getItems().clear();
            categoricalValueCombo.getSelectionModel().clearSelection();

            if (newVal == null) {
                return;
            }

            if (newVal.equals(FEATURE_PSIONIC)) {
                // Psionic Interference Tolerance → operator + numeric value
                operatorCombo.setVisible(true);
                operatorCombo.setManaged(true);

                valueField.setVisible(true);
                valueField.setManaged(true);
                valueField.setPromptText("psionic value (e.g. 0.5)");

            } else if (CATEGORICAL_VALUES.containsKey(newVal)) {
                // QCT, Symbiotic, ATDR, BLT → categorical values
                List<String> cats = CATEGORICAL_VALUES.get(newVal);

                categoricalValueCombo.setItems(FXCollections.observableArrayList(cats));
                if (!cats.isEmpty()) {
                    categoricalValueCombo.getSelectionModel().selectFirst();
                }

                categoricalValueCombo.setVisible(true);
                categoricalValueCombo.setManaged(true);
            } else if (newVal.equals(FEATURE_NG)) {
                // NG filter: no extra input needed
                // leave all secondary controls hidden
            }
        });

        HBox.setHgrow(valueField, Priority.ALWAYS);
        HBox.setHgrow(featureCombo, Priority.SOMETIMES);
        HBox.setHgrow(categoricalValueCombo, Priority.ALWAYS);


        row.getChildren().addAll(
                featureCombo,
                operatorCombo,
                valueField,
                categoricalValueCombo
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
    Node createDashboardArea() {
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
    HBox createStatusBar() {
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
    void initDatasets() {
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
    void populateCourseList() {
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

        String chosenFeatureType = (featureCombo != null) ? featureCombo.getValue() : null;
        String chosenFeatureValue = (categoricalValueCombo != null) ? categoricalValueCombo.getValue() : null;

// For categorical features (QCT, Symbiotic, ATDR, BLT) we use chosenFeatureValue.
// For Psionic we use the numeric operator+value from buildPsionicFeatureString().
        boolean hasCategoricalFeatureValue =
                chosenFeatureValue != null && !chosenFeatureValue.isEmpty();

        boolean hasPsionicNumericValue =
                FEATURE_PSIONIC.equals(chosenFeatureType)
                        && buildPsionicFeatureString() != null;


        boolean splitByFeature = splitByFeatureCheckBox != null
                && splitByFeatureCheckBox.isSelected()
                && chosenFeatureType != null
                && !FEATURE_NG.equals(chosenFeatureType) // NG is handled separately
                && (hasCategoricalFeatureValue || hasPsionicNumericValue);



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

        // NG filter only applies to BAR and HISTOGRAM.
        if (FEATURE_NG.equals(chosenFeatureType)
                && (plotType == PlotType.SCATTER
                || plotType == PlotType.SWARM
                || plotType == PlotType.JOINT)) {
            setStatus("'NG' filter is only available for BAR and HISTOGRAM plots. Ignoring filter for " + plotType + ".");
            chosenFeatureType = null;
        }


        switch (plotType) {
            case BAR:
                handleBarPlot(datasetDisplay, chosenCourses, gradesData, courseNames,
                        chosenFeatureType, chosenFeatureValue, splitByFeature);

                break;
            case HISTOGRAM:
                handleHistogramPlot(datasetDisplay, chosenCourses, gradesData, courseNames,
                        chosenFeatureType, chosenFeatureValue, splitByFeature);
                break;
            case SCATTER:
                handleScatterPlot(datasetDisplay, chosenCourses, gradesData, courseNames);
                break;
            case SWARM:
                handleSwarmPlot(datasetDisplay, chosenCourses, gradesData, courseNames, chosenFeatureType,
                        chosenFeatureValue, splitByFeature);
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
                               String featureType,
                               String featureValue,
                               boolean splitByFeature) {

        if (FEATURE_NG.equals(featureType)) {
            Map<String, Integer> ngCounts =
                    buildNgCountsMap(chosenCourses, gradesData, courseNames);

            if (ngCounts.isEmpty()) {
                for (int i = 0; i < courseViews.length; i++) {
                    courseViews[i].setTitle("NG counts");
                    courseViews[i].setPlaceholderText("No NG entries for selected courses.");
                    if (i == 0) {
                        courseViews[i].setVisible(true);
                        courseViews[i].setManaged(true);
                    } else {
                        courseViews[i].setVisible(false);
                        courseViews[i].setManaged(false);
                    }
                }
                setStatus("No NG grades found for selected courses.");
                return;
            }

            BarChart<String, Number> ngChart =
                    createNgBarChart(ngCounts, "Number of NG grades – " + datasetDisplay);

            courseViews[0].setTitle("NG counts per course");
            courseViews[0].setContent(ngChart);
            courseViews[0].setVisible(true);
            courseViews[0].setManaged(true);

            for (int i = 1; i < courseViews.length; i++) {
                courseViews[i].setVisible(false);
                courseViews[i].setManaged(false);
            }

            setStatus("Rendered NG count BAR chart for " + ngCounts.keySet() + " [" + datasetDisplay + "]");
            return;
        }
        String effectiveFeature = null;
        if (FEATURE_PSIONIC.equals(featureType)) {
            effectiveFeature = buildPsionicFeatureString();
        } else {
            effectiveFeature = featureValue;
        }


        if (!splitByFeature) {
            // ORIGINAL behaviour: single population (possibly filtered)
            Map<String, double[]> courseGradesMap =
                    buildCourseGradesMap(chosenCourses, gradesData, courseNames, effectiveFeature);

            if (courseGradesMap.isEmpty()) {
                for (VisualizationPane vp : courseViews) {
                    vp.setPlaceholderText("No valid grades for selected courses.");
                    vp.setVisible(false);
                    vp.setManaged(false);
                }
                setStatus("No valid grades for selected courses.");
                return;
            }
            BarChart<String, Number> chart = createMultiCourseBarChart(courseGradesMap, "Grade distribution – " + datasetDisplay);

            courseViews[0].setTitle("Bar chart – selected courses");
            courseViews[0].setContent(chart);
            courseViews[0].setVisible(true);
            courseViews[0].setManaged(true);

            for (int i = 1; i < courseViews.length; i++) {
                courseViews[i].setVisible(false);
                courseViews[i].setManaged(false);
            }

            setStatus("Rendered BAR chart for " + courseGradesMap.keySet() + " [" + datasetDisplay + "]");
            return;
        }

        // NEW: split by feature (with vs without)
        SplitCourseMaps splitMaps =
                buildCourseGradesMapSplit(chosenCourses, gradesData, courseNames, effectiveFeature);

        Map<String, double[]> withFeatureMap = splitMaps.withFeature;
        Map<String, double[]> withoutFeatureMap = splitMaps.withoutFeature;

        if (withFeatureMap.isEmpty() && withoutFeatureMap.isEmpty()) {

            for (VisualizationPane vp : courseViews) {
                vp.setPlaceholderText("No valid grades for selected courses.");
                vp.setVisible(false);
                vp.setManaged(false);
            }
            setStatus("No valid grades for selected courses (with/without feature).");
            return;
        }// Pane 0: WITH feature
        if (!withFeatureMap.isEmpty()) {
            BarChart<String, Number> chartWith =
                    createMultiCourseBarChart(withFeatureMap,
                            "Grade distribution – " + datasetDisplay + " (with feature)");
            courseViews[0].setTitle("Bar chart – WITH feature: " + featureValue);
            courseViews[0].setContent(chartWith);
        } else {
            courseViews[0].setTitle("Bar chart – WITH feature: " + featureValue);
            courseViews[0].setPlaceholderText("No data WITH this feature.");
        }
        courseViews[0].setVisible(true);
        courseViews[0].setManaged(true);

        // Pane 1: WITHOUT feature
        if (!withoutFeatureMap.isEmpty()) {
            BarChart<String, Number> chartWithout =
                    createMultiCourseBarChart(withoutFeatureMap,
                            "Grade distribution – " + datasetDisplay + " (without feature)");
            courseViews[1].setTitle("Bar chart – WITHOUT feature");
            courseViews[1].setContent(chartWithout);
        } else {
            courseViews[1].setTitle("Bar chart – WITHOUT feature");
            courseViews[1].setPlaceholderText("No data WITHOUT this feature.");
        }
        courseViews[1].setVisible(true);
        courseViews[1].setManaged(true);

        // Hide remaining panes
        for (int i = 2; i < courseViews.length; i++) {
            courseViews[i].setVisible(false);
            courseViews[i].setManaged(false);
        }

        setStatus("Rendered BAR chart (with vs without feature) for " + chosenCourses + " [" + datasetDisplay + "]");
    }

    // ---------------------------------------------------------------------
    // HISTOGRAM: 1–4 separate histograms (same logic as before)
    // ---------------------------------------------------------------------
    private void handleHistogramPlot(String datasetDisplay,
                                     List<String> chosenCourses,
                                     String[][] gradesData,
                                     String[] courseNames,
                                     String featureType,
                                     String chosenFeature,
                                     boolean splitByFeature) {

        if (FEATURE_NG.equals(featureType)) {
            Map<String, Integer> ngCounts =
                    buildNgCountsMap(chosenCourses, gradesData, courseNames);

            if (ngCounts.isEmpty()) {
                for (int i = 0; i < courseViews.length; i++) {
                    courseViews[i].setTitle("NG counts");
                    courseViews[i].setPlaceholderText("No NG entries for selected courses.");
                    if (i == 0) {
                        courseViews[i].setVisible(true);
                        courseViews[i].setManaged(true);
                    } else {
                        courseViews[i].setVisible(false);
                        courseViews[i].setManaged(false);
                    }
                }
                setStatus("No NG grades found for selected courses.");
                return;
            }

            BarChart<String, Number> ngChart =
                    createNgBarChart(ngCounts, "Number of NG grades – " + datasetDisplay);

            courseViews[0].setTitle("NG counts per course");
            courseViews[0].setContent(ngChart);
            courseViews[0].setVisible(true);
            courseViews[0].setManaged(true);

            for (int i = 1; i < courseViews.length; i++) {
                courseViews[i].setVisible(false);
                courseViews[i].setManaged(false);
            }

            setStatus("Rendered NG count HISTOGRAM (as bar chart) for " + ngCounts.keySet() + " [" + datasetDisplay + "]");
            return;
        }

        String effectiveFeature = null;
        if (FEATURE_PSIONIC.equals(featureType)) {
            effectiveFeature = buildPsionicFeatureString();
        } else {
            effectiveFeature = chosenFeature;
        }


        if (!splitByFeature) {
            Map<String, double[]> courseGradesMap =
                    buildCourseGradesMap(chosenCourses, gradesData, courseNames, effectiveFeature);

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
            return;
        }

        // Split by feature
        SplitCourseMaps splitMaps =
                buildCourseGradesMapSplit(chosenCourses, gradesData, courseNames, effectiveFeature);

        Map<String, double[]> withFeatureMap = splitMaps.withFeature;
        Map<String, double[]> withoutFeatureMap = splitMaps.withoutFeature;

        if (withFeatureMap.isEmpty() && withoutFeatureMap.isEmpty()) {

            for (VisualizationPane vp : courseViews) {
                vp.setPlaceholderText("No valid grades for selected courses.");
                vp.setVisible(false);
                vp.setManaged(false);
            }
            setStatus("No valid grades for selected courses (with/without feature).");
            return;
        }

        int paneIndex = 0;
        // First: WITH feature
        for (Map.Entry<String, double[]> entry : withFeatureMap.entrySet()) {
            if (paneIndex >= courseViews.length) break;
            String courseName = entry.getKey();
            double[] grades = entry.getValue();

            if (grades.length == 0) {
                courseViews[paneIndex].setTitle(courseName + " (WITH feature)");
                courseViews[paneIndex].setPlaceholderText("No valid grades WITH feature for: " + courseName);
            } else {
                BarChart<String, Number> hist = createHistogramChart(
                        grades,
                        "Grade distribution – " + courseName + " (WITH feature)"
                );
                courseViews[paneIndex].setTitle(courseName + " (WITH feature)");
                courseViews[paneIndex].setContent(hist);
            }

            courseViews[paneIndex].setVisible(true);
            courseViews[paneIndex].setManaged(true);
            paneIndex++;
        }

        // Then: WITHOUT feature
        for (Map.Entry<String, double[]> entry : withoutFeatureMap.entrySet()) {

            if (paneIndex >= courseViews.length) break;
            String courseName = entry.getKey();
            double[] grades = entry.getValue();

            if (grades.length == 0) {
                courseViews[paneIndex].setTitle(courseName + " (WITHOUT feature)");
                courseViews[paneIndex].setPlaceholderText("No valid grades WITHOUT feature for: " + courseName);
            } else {
                BarChart<String, Number> hist = createHistogramChart(
                        grades,
                        "Grade distribution – " + courseName + " (WITHOUT feature)"
                );
                courseViews[paneIndex].setTitle(courseName + " (WITHOUT feature)");
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

        setStatus("Rendered HISTOGRAM (with vs without feature) for " + chosenCourses + " [" + datasetDisplay + "]");
    }

    // SCATTER: must choose EXACTLY 2 courses - FIXED METHOD SIGNATURE
    private void handleScatterPlot(String datasetDisplay,
                                   List<String> chosenCourses,
                                   String[][] gradesData,
                                   String[] courseNames) {

        // FIXED: Remove references to undefined variables and fix method logic
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

    // SWARM: 1–4 courses in one swarm-like scatter plot - FIXED METHOD SIGNATURE
    private void handleSwarmPlot(String datasetDisplay,
                                 List<String> chosenCourses,
                                 String[][] gradesData,
                                 String[] courseNames,
                                 String chosenFeature,
                                 String effectiveFeature,
                                 boolean splitByFeature) {

        if (!splitByFeature) {
            Map<String, double[]> courseGradesMap =
                    buildCourseGradesMap(chosenCourses, gradesData, courseNames, effectiveFeature);

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
            return;
        }

        // Split by feature
        SplitCourseMaps splitMaps =
                buildCourseGradesMapSplit(chosenCourses, gradesData, courseNames, effectiveFeature);


        Map<String, double[]> withFeatureMap = splitMaps.withFeature;
        Map<String, double[]> withoutFeatureMap = splitMaps.withoutFeature;

        if (withFeatureMap.isEmpty() && withoutFeatureMap.isEmpty()) {
            courseViews[0].setTitle("Swarm plot");
            courseViews[0].setPlaceholderText("No valid grades for selected courses.");
            courseViews[0].setVisible(true);
            courseViews[0].setManaged(true);

            for (int i = 1; i < courseViews.length; i++) {
                courseViews[i].setVisible(false);
                courseViews[i].setManaged(false);
            }

            setStatus("No valid grades for swarm plot (with/without feature).");
            return;
        }

        // Pane 0: WITH feature
        if (!withFeatureMap.isEmpty()) {
            ScatterChart<Number, Number> swarmWith =
                    createSwarmChart(withFeatureMap,
                            "Swarm plot – " + datasetDisplay + " (WITH feature)");
            courseViews[0].setTitle("Swarm plot – WITH feature: " + chosenFeature);
            courseViews[0].setContent(swarmWith);
        } else {
            courseViews[0].setTitle("Swarm plot – WITH feature: " + chosenFeature);
            courseViews[0].setPlaceholderText("No data WITH this feature.");
        }

        courseViews[0].setVisible(true);
        courseViews[0].setManaged(true);

        // Pane 1: WITHOUT feature
        if (!withoutFeatureMap.isEmpty()) {
            ScatterChart<Number, Number> swarmWithout =
                    createSwarmChart(withoutFeatureMap,
                            "Swarm plot – " + datasetDisplay + " (WITHOUT feature)");
            courseViews[1].setTitle("Swarm plot – WITHOUT feature");
            courseViews[1].setContent(swarmWithout);
        } else {
            courseViews[1].setTitle("Swarm plot – WITHOUT feature");
            courseViews[1].setPlaceholderText("No data WITHOUT this feature.");
        }
        courseViews[1].setVisible(true);
        courseViews[1].setManaged(true);

        for (int i = 2; i < courseViews.length; i++) {
            courseViews[i].setVisible(false);
            courseViews[i].setManaged(false);
        }

        setStatus("Rendered SWARM plot (with vs without feature) for " + chosenCourses + " [" + datasetDisplay + "]");
    }

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

        String courseX = chosenCourses.get(0);
        String courseY = chosenCourses.get(1);

        int colX = findCourseColumn(courseNames, courseX);
        int colY = findCourseColumn(courseNames, courseY);

        if (colX < 0 || colY < 0) {
            courseViews[0].setTitle("Joint plot: " + courseX + " vs " + courseY);
            courseViews[0].setPlaceholderText("Could not find one or both courses in the dataset.");
            courseViews[0].setVisible(true);
            courseViews[0].setManaged(true);
            for (int i = 1; i < courseViews.length; i++) {
                courseViews[i].setVisible(false);
                courseViews[i].setManaged(false);
            }
            setStatus("Course not found for joint plot.");
            return;
        }

        // Collect paired grades
        double[][] xy = collectPairedGrades(gradesData, colX, colY);
        double[] xs = xy[0];
        double[] ys = xy[1];

        if (xs.length == 0) {
            courseViews[0].setTitle("Joint plot: " + courseX + " vs " + courseY);
            courseViews[0].setPlaceholderText("No paired grades available for selected courses.");
            courseViews[0].setVisible(true);
            courseViews[0].setManaged(true);

            for (int i = 1; i < courseViews.length; i++) {
                courseViews[i].setVisible(false);
                courseViews[i].setManaged(false);
            }
            setStatus("No paired grades for joint plot.");
            return;
        }

        // Create the scatter plot (center of joint plot)
        ScatterChart<Number, Number> scatterChart = createScatterChart(xs, ys, courseX, courseY);

        // Set up the joint plot container
        BorderPane jointPlotContainer = new BorderPane();
        jointPlotContainer.setCenter(scatterChart);
        jointPlotContainer.setStyle("-fx-background-color: white;");

        // This ensures histograms are built AFTER the chart is actually drawn
        scatterChart.sceneProperty().addListener((obsScene, oldScene, newScene) -> {
            if (newScene != null) {
                scatterChart.layout(); // force layout pass

                Platform.runLater(() -> {

                    Node plotArea = scatterChart.lookup(".chart-plot-background");
                    if (plotArea == null) return;

                    Bounds pb = plotArea.getBoundsInParent();

                    double topHeight = 80;
                    double rightWidth = 80;

                    rectangleMaker maker = new rectangleMaker();

                    Rectangle[] topRects = maker.createMarginalHistograms(
                            xs, 1, 10, pb.getWidth(), topHeight, true
                    );

                    Rectangle[] rightRects = maker.createMarginalHistograms(
                            ys, 1, 10, rightWidth, pb.getHeight(), false
                    );

                    // --- Top histogram pane ---
                    Pane topPane = new Pane(topRects);
                    topPane.setMinHeight(topHeight);
                    topPane.setPrefHeight(topHeight);
                    topPane.setMaxHeight(topHeight);
                    topPane.setStyle("-fx-background-color: rgba(0,0,0,0.05);"); // debug

                    // --- Right histogram pane ---
                    Pane rightPane = new Pane(rightRects);
                    rightPane.setMinWidth(rightWidth);
                    rightPane.setPrefWidth(rightWidth);
                    rightPane.setMaxWidth(rightWidth);
                    rightPane.setStyle("-fx-background-color: rgba(0,0,0,0.05);"); // debug

                    jointPlotContainer.setTop(topPane);
                    jointPlotContainer.setRight(rightPane);
                });
            }
        });

        courseViews[0].setTitle("Joint plot: " + courseX + " vs " + courseY);
        courseViews[0].setContent(jointPlotContainer);
        courseViews[0].setVisible(true);
        courseViews[0].setManaged(true);

        for (int i = 1; i < courseViews.length; i++) {
            courseViews[i].setVisible(false);
            courseViews[i].setManaged(false);
        }

        setStatus("Rendered JOINT plot for [" + courseX + " vs " + courseY + "] in " + datasetDisplay);
    }


    // ---------------------------------------------------------------------
    // Helpers for building course → grades map
    // ---------------------------------------------------------------------
    private Map<String, double[]> buildCourseGradesMap(List<String> chosenCourses,
                                                       String[][] gradesData,
                                                       String[] courseNames,
                                                       String chosenFeature) {
        Map<String, double[]> courseGradesMap = new LinkedHashMap<>();

        // Use Information only if it matches gradesData row count
        // and we actually have a feature selected.
        boolean canUseStudentInfo =
                studentInformation != null
                        && gradesData != null
                        && studentInformation.length == gradesData.length
                        && chosenFeature != null
                        && !chosenFeature.isEmpty();


        for (String courseName : chosenCourses) {
            int courseColumn = findCourseColumn(courseNames, courseName);
            if (courseColumn < 0) {
                continue;
            }

            phase2Handler handler;
            if (canUseStudentInfo) {
                // Filtered data (uses StudentInfo + chosen feature)
                handler = new phase2Handler(gradesData, studentInformation, courseColumn);
                handler.setChosenDataWithDataFeature(chosenFeature);
            } else {
                // Fallback: no studentInformation / no feature → no filter
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

    // Helper method to collect all grades from a column (fallback)
    private double[] collectAllGrades(String[][] data, int column) {
        if (data == null || column < 0) return new double[0];
        List<Double> grades = new ArrayList<>();
        for (String[] row : data) {
            if (row != null && column < row.length) {
                String gradeStr = row[column];
                if (gradeStr != null && !gradeStr.equalsIgnoreCase("NG")) {
                    try {
                        grades.add(Double.parseDouble(gradeStr));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        double[] result = new double[grades.size()];
        for (int i = 0; i < grades.size(); i++) {
            result[i] = grades.get(i);
        }
        return result;
    }

    // Helper holder for with-feature vs without-feature maps
    private static class SplitCourseMaps {
        Map<String, double[]> withFeature = new LinkedHashMap<>();
        Map<String, double[]> withoutFeature = new LinkedHashMap<>();
    }

    /**
     * Uses phase2Handler.getSplitData(feature) to build:
     * - withFeature: grades for students having the feature
     * - withoutFeature: grades for students NOT having the feature
     */
    private SplitCourseMaps buildCourseGradesMapSplit(List<String> chosenCourses,
                                                      String[][] gradesData,
                                                      String[] courseNames,
                                                      String feature) {
        SplitCourseMaps result = new SplitCourseMaps();

        if (gradesData == null ||
                studentInformation == null ||  // field from your class
                feature == null ||
                feature.isEmpty()) {
            return result;
        }

        // ---------- Detect Psionic numeric condition ("greater X" / "smaller X") ----------
        int pitChecker = 0;     // 1 = >=, 2 = <=
        double pitFeature = 0;
        final int PIT_COLUMN = 3; // matches dataChooser.getFeatureColumn() PIT branch

        try (Scanner scanner = new Scanner(feature)) {
            if (scanner.hasNext("greater")) {
                pitChecker = 1;
                while (!scanner.hasNextDouble() && scanner.hasNext()) {
                    scanner.next();
                }
                if (scanner.hasNextDouble()) {
                    pitFeature = scanner.nextDouble();
                }
            } else if (scanner.hasNext("smaller")) {
                pitChecker = 2;
                while (!scanner.hasNextDouble() && scanner.hasNext()) {
                    scanner.next();
                }
                if (scanner.hasNextDouble()) {
                    pitFeature = scanner.nextDouble();
                }
            }
        } catch (Exception ignored) {
        }

        boolean isPsionicNumeric = (pitChecker != 0);

        // ---------- Psionic numeric split: do it manually on StudentInfo ----------
        if (isPsionicNumeric) {
            for (String courseName : chosenCourses) {
                int courseColumn = findCourseColumn(courseNames, courseName);
                if (courseColumn < 0) continue;

                List<Double> withList = new ArrayList<>();
                List<Double> withoutList = new ArrayList<>();

                for (int i = 0; i < gradesData.length; i++) {
                    String[] row = gradesData[i];
                    if (row == null || courseColumn >= row.length) continue;

                    String gradeStr = row[courseColumn];
                    if (gradeStr == null || gradeStr.equalsIgnoreCase("NG")) continue;

                    if (i >= studentInformation.length ||
                            studentInformation[i] == null ||
                            PIT_COLUMN >= studentInformation[i].length) {
                        continue;
                    }

                    String pitStr = studentInformation[i][PIT_COLUMN];
                    if (pitStr == null || pitStr.isEmpty()) continue;

                    try {
                        double pitVal = Double.parseDouble(pitStr);
                        boolean inWith;
                        if (pitChecker == 1) {          // "greater" => >=
                            inWith = pitVal >= pitFeature;
                        } else {                         // "smaller" => <=
                            inWith = pitVal <= pitFeature;
                        }

                        double gradeVal = Double.parseDouble(gradeStr);
                        if (inWith) {
                            withList.add(gradeVal);
                        } else {
                            withoutList.add(gradeVal);
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }

                if (!withList.isEmpty()) {
                    double[] arr = new double[withList.size()];
                    for (int k = 0; k < withList.size(); k++) arr[k] = withList.get(k);
                    result.withFeature.put(courseName, arr);
                }
                if (!withoutList.isEmpty()) {
                    double[] arr = new double[withoutList.size()];
                    for (int k = 0; k < withoutList.size(); k++) arr[k] = withoutList.get(k);
                    result.withoutFeature.put(courseName, arr);
                }
            }

            return result;
        }

        // ---------- Categorical features: original phase2Handler-based split ----------
        for (String courseName : chosenCourses) {
            int courseColumn = findCourseColumn(courseNames, courseName);
            if (courseColumn < 0) continue;

            try {
                // Check if phase2Handler exists
                Class.forName("org.example.phase2Handler");
                phase2Handler handler = new phase2Handler(gradesData, studentInformation, courseColumn);
                int[][] splitIndices = handler.getSplitData(feature);
                int[] withIdx = splitIndices[0];
                int[] withoutIdx = splitIndices[1];

                double[] withGrades = collectGradesByIndices(gradesData, courseColumn, withIdx);
                double[] withoutGrades = collectGradesByIndices(gradesData, courseColumn, withoutIdx);

                if (withGrades.length > 0) {
                    result.withFeature.put(courseName, withGrades);
                }
                if (withoutGrades.length > 0) {
                    result.withoutFeature.put(courseName, withoutGrades);
                }
            } catch (ClassNotFoundException e) {
                // If phase2Handler doesn't exist, return empty result
                System.out.println("phase2Handler not found, cannot split by feature");
                return result;
            } catch (Exception e) {
                System.out.println("Error splitting data by feature: " + e.getMessage());
                return result;
            }
        }

        return result;
    }

    /**
     * Collect grades for given row indices in a single course column.
     */
    private double[] collectGradesByIndices(String[][] data, int courseColumn, int[] indices) {
        if (data == null || indices == null) return new double[0];

        List<Double> values = new ArrayList<>();

        for (int idx : indices) {
            if (idx < 0 || idx >= data.length) continue;
            String[] row = data[idx];
            if (row == null || courseColumn < 0 || courseColumn >= row.length) continue;

            String g = row[courseColumn];
            if (g == null || g.equalsIgnoreCase("NG")) continue;

            try {
                values.add(Double.parseDouble(g));
            } catch (NumberFormatException ignored) {
            }
        }

        double[] result = new double[values.size()];
        for (int i = 0; i < values.size(); i++) {
            result[i] = values.get(i);
        }
        return result;
    }

    private Map<String, Integer> buildNgCountsMap(List<String> chosenCourses,
                                                  String[][] gradesData,
                                                  String[] courseNames) {
        Map<String, Integer> ngCounts = new LinkedHashMap<>();
        if (gradesData == null || courseNames == null || chosenCourses == null) {
            return ngCounts;
        }

        for (String courseName : chosenCourses) {
            int col = findCourseColumn(courseNames, courseName);
            if (col < 0) continue;

            int count = 0;
            for (String[] row : gradesData) {
                if (row == null) continue;
                if (col >= row.length) continue;
                String g = row[col];
                if (g != null && g.equalsIgnoreCase("NG")) {
                    count++;
                }
            }
            ngCounts.put(courseName, count);
        }

        return ngCounts;
    }

    // NEW: bar chart for NG counts per course
    private BarChart<String, Number> createNgBarChart(Map<String, Integer> ngCounts,
                                                      String title) {
        CategoryAxis xAxis = new CategoryAxis();
        ObservableList<String> categories =
                FXCollections.observableArrayList(ngCounts.keySet());
        xAxis.setCategories(categories);
        xAxis.setLabel("Course");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of NG grades");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("NG count");

        for (Map.Entry<String, Integer> entry : ngCounts.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        chart.getData().add(series);
        return chart;
    }


    // NEW: always use ALL students (no feature filter)
    private Map<String, double[]> buildCourseGradesMapUnfiltered(List<String> chosenCourses,
                                                                 String[][] gradesData,
                                                                 String[] courseNames) {
        Map<String, double[]> courseGradesMap = new LinkedHashMap<>();

        for (String courseName : chosenCourses) {
            int courseColumn = findCourseColumn(courseNames, courseName);
            if (courseColumn < 0) {
                continue;
            }

            double[] grades = collectAllGrades(gradesData, courseColumn);
            if (grades.length > 0) {
                courseGradesMap.put(courseName, grades);
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
        if (courseNames == null || courseName == null) return -1;
        String target = courseName.trim();
        for (int i = 0; i < courseNames.length; i++) {
            if (courseNames[i] != null &&
                    courseNames[i].trim().equalsIgnoreCase(target)) {

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
        // Updated labels: 1 to 10
        String[] labels = {
                "1","2","3","4","5","6","7","8","9","10"
        };
        int[] counts = new int[10];

        for (double g : grades) {
            int idx = (int) g - 1;
            if (idx >= 0 && idx < counts.length) {
                counts[idx]++;
            }
        }

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Grade");
        xAxis.setCategories(FXCollections.observableArrayList(labels));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Number of students");

        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle(title);
        chart.setLegendVisible(false);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (int i = 0; i < counts.length; i++) {
            series.getData().add(new XYChart.Data<>(labels[i], counts[i]));
        }
        chart.getData().add(series);
        return chart;
    }

    // Multi-course bar chart: each course is a series
    private BarChart<String, Number> createMultiCourseBarChart(Map<String, double[]> courseGradesMap,
                                                               String title) {
        // Updated labels: 1 to 10
        String[] labels = {
                "1","2","3","4","5","6","7","8","9","10"
        };

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Grade");
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
                int idx = (int) g - 1;
                if (idx >= 0 && idx < counts.length) {
                    counts[idx]++;
                }
            }

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName(courseName);
            for (int i = 0; i < labels.length; i++) {
                series.getData().add(new XYChart.Data<>(labels[i], counts[i]));
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
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(1);
        xAxis.setUpperBound(10);


        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel(courseY);
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(1);
        yAxis.setUpperBound(10);


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
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(1);
        yAxis.setUpperBound(10);


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

    // Helper: build feature string for Psionic Interference Tolerance,
// used by phase2Handler/dataChooser (checkPIT expects "greater"/"smaller" + number).
    private String buildPsionicFeatureString() {
        if (operatorCombo == null || valueField == null) return null;

        String op = operatorCombo.getValue();
        String val = valueField.getText();
        if (op == null || val == null || val.trim().isEmpty()) {
            return null;
        }

        String keyword;
        switch (op) {
            case ">":
            case ">=":
            case "=":      // treat '=' approximately as ">="
                keyword = "greater";
                break;
            case "<":
            case "<=":
                keyword = "smaller";
                break;
            default:
                keyword = "greater";
        }
        return keyword + " " + val.trim();
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
}