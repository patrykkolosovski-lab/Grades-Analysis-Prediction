package org.example.ui;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.example.analytics.CategoricalFeatureFilter;
import org.example.analytics.FeatureFilter;
import org.example.analytics.FeatureType;
import org.example.analytics.NoFilter;
import org.example.analytics.NumericThresholdFilter;
import org.example.analytics.PlotData;
import org.example.analytics.PlotDataFactory;
import org.example.analytics.PlotRequest;
import org.example.data.AppData;
import org.example.data.DatasetType;
import org.example.ui.render.GroupedBarRenderer;
import org.example.ui.render.HistogramRenderer;
import org.example.ui.render.JointRenderer;
import org.example.ui.render.MessageRenderer;
import org.example.ui.render.MissingGradesRenderer;
import org.example.ui.render.ScatterRenderer;
import org.example.ui.render.SwarmRenderer;

import java.util.ArrayList;
import java.util.List;

/**
 * Coordinates dashboard state, validation, and rendering.
 */
public final class DashboardController {
    private final DashboardView view;
    private final PlotDataFactory plotDataFactory;
    private final HistogramRenderer histogramRenderer;
    private final GroupedBarRenderer groupedBarRenderer;
    private final ScatterRenderer scatterRenderer;
    private final SwarmRenderer swarmRenderer;
    private final JointRenderer jointRenderer;
    private final MissingGradesRenderer missingGradesRenderer;
    private final MessageRenderer messageRenderer;
    private final AppData appData;

    public DashboardController(AppData appData) {
        this.view = new DashboardView();
        this.plotDataFactory = new PlotDataFactory();
        this.histogramRenderer = new HistogramRenderer();
        this.groupedBarRenderer = new GroupedBarRenderer();
        this.scatterRenderer = new ScatterRenderer();
        this.swarmRenderer = new SwarmRenderer();
        this.jointRenderer = new JointRenderer();
        this.missingGradesRenderer = new MissingGradesRenderer();
        this.messageRenderer = new MessageRenderer();
        this.appData = appData;
        bind();
        refreshCourses();
        refreshFilterUi();
        render();
    }

    public Parent root() {
        return view;
    }

    private void bind() {
        view.datasetComboBox().valueProperty().addListener((obs, oldValue, newValue) -> {
            refreshCourses();
            refreshFilterUi();
            render();
        });
        view.featureTypeComboBox().valueProperty().addListener((obs, oldValue, newValue) -> refreshFilterUi());
        view.renderButton().setOnAction(event -> render());
        view.clearButton().setOnAction(event -> clearSelections());
    }

    private void refreshCourses() {
        DatasetType datasetType = selectedDatasetType();
        view.setCourses(appData.dataset(datasetType).courseNames());
        view.setStatus("Loaded " + datasetType.label() + " dataset.");
    }

    private void refreshFilterUi() {
        DatasetType datasetType = selectedDatasetType();
        boolean supportsFilters = appData.supportsFeatureFilters(datasetType);
        view.featureTypeComboBox().setDisable(!supportsFilters);
        view.categoricalValueComboBox().setDisable(!supportsFilters);
        view.comparisonOperatorComboBox().setDisable(!supportsFilters);
        view.thresholdField().setDisable(!supportsFilters);
        if (!supportsFilters) {
            view.featureTypeComboBox().getSelectionModel().select(FeatureType.NONE);
            view.setFilterControlsVisible(false, false);
            view.filterHelpLabel().setText("Feature filters are available only for the Current dataset.");
            return;
        }

        FeatureType featureType = view.featureTypeComboBox().getValue();
        if (featureType == null || featureType == FeatureType.NONE) {
            view.setFilterControlsVisible(false, false);
            view.filterHelpLabel().setText("No filter applied.");
            return;
        }

        if (featureType.isCategorical()) {
            view.categoricalValueComboBox().setItems(FXCollections.observableArrayList(featureType.categories()));
            if (!featureType.categories().isEmpty()) {
                view.categoricalValueComboBox().getSelectionModel().selectFirst();
            }
            view.setFilterControlsVisible(true, false);
            view.filterHelpLabel().setText("Filtering Current students by " + featureType.label() + ".");
            return;
        }

        view.setFilterControlsVisible(false, true);
        view.filterHelpLabel().setText("Filtering Current students by a numeric psionic threshold.");
    }

    private void render() {
        PlotData plotData = plotDataFactory.create(
                appData,
                new PlotRequest(selectedDatasetType(), view.plotTypeComboBox().getValue(), limitedCourses(), buildFilter())
        );
        view.showResult(renderPlot(plotData));
        if (plotData instanceof PlotData.MessagePlotData messagePlotData) {
            view.setStatus(messagePlotData.message());
        } else {
            view.setStatus("Rendered " + view.plotTypeComboBox().getValue() + " for " + selectedDatasetType().label() + ".");
        }
    }

    private Node renderPlot(PlotData plotData) {
        if (plotData instanceof PlotData.HistogramPlotData histogramPlotData) {
            return histogramRenderer.render(histogramPlotData);
        }
        if (plotData instanceof PlotData.GroupedBarPlotData groupedBarPlotData) {
            return groupedBarRenderer.render(groupedBarPlotData);
        }
        if (plotData instanceof PlotData.ScatterPlotData scatterPlotData) {
            return scatterRenderer.render(scatterPlotData);
        }
        if (plotData instanceof PlotData.SwarmPlotData swarmPlotData) {
            return swarmRenderer.render(swarmPlotData);
        }
        if (plotData instanceof PlotData.JointPlotData jointPlotData) {
            return jointRenderer.render(jointPlotData);
        }
        if (plotData instanceof PlotData.MissingGradesPlotData missingGradesPlotData) {
            return missingGradesRenderer.render(missingGradesPlotData);
        }
        return messageRenderer.render((PlotData.MessagePlotData) plotData);
    }

    private FeatureFilter buildFilter() {
        FeatureType featureType = view.featureTypeComboBox().getValue();
        if (featureType == null || featureType == FeatureType.NONE) {
            return new NoFilter();
        }
        if (featureType.isCategorical()) {
            String value = view.categoricalValueComboBox().getValue();
            if (value == null || value.isBlank()) {
                return new NoFilter();
            }
            return new CategoricalFeatureFilter(featureType, value);
        }
        String threshold = view.thresholdField().getText();
        if (threshold == null || threshold.isBlank()) {
            return new NoFilter();
        }
        try {
            return new NumericThresholdFilter(
                    featureType,
                    view.comparisonOperatorComboBox().getValue(),
                    Double.parseDouble(threshold.trim())
            );
        } catch (NumberFormatException exception) {
            view.setStatus("The numeric filter must be a valid number.");
            return new NoFilter();
        }
    }

    private DatasetType selectedDatasetType() {
        DatasetType datasetType = view.datasetComboBox().getValue();
        return datasetType == null ? DatasetType.CURRENT : datasetType;
    }

    private List<String> limitedCourses() {
        List<String> selected = new ArrayList<>(view.selectedCourses());
        int maxCourses = view.maxCoursesSpinner().getValue();
        if (selected.size() <= maxCourses) {
            return selected;
        }
        return selected.subList(0, maxCourses);
    }

    private void clearSelections() {
        view.courseListView().getSelectionModel().clearSelection();
        view.featureTypeComboBox().getSelectionModel().select(FeatureType.NONE);
        view.thresholdField().clear();
        refreshFilterUi();
        render();
    }
}
