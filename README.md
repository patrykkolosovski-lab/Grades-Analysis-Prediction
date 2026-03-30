# Grade Dashboard

JavaFX dashboard for exploring student grade data and predicting subject outcomes with a random-forest model.

## Overview

Grade Dashboard combines two workflows in one desktop application:

- interactive data analysis for current and graduate grade datasets
- multiple chart types for comparing courses and grade distributions
- feature filtering to narrow the plotted population
- per-student grade prediction for a selected subject
- holdout-set evaluation with mean squared error reporting

## Tech Stack

- Java 24
- JavaFX
- Maven
- Custom random-forest implementation

## Run Locally

```bash
mvn javafx:run
```

Bundled CSV datasets are stored in `src/main/resources/`.

## Demo Presentation

### 1. Landing screen

The application opens with a simple entry point that separates exploratory analysis from prediction tasks.

<p align="center">
  <img src="docs/Screenshot%202026-03-30%20at%2010.23.26.png" alt="Grade Dashboard landing screen" width="900">
</p>

### 2. Data analysis workspace

The analysis view lets you choose a dataset, control how many courses are rendered, and inspect distributions across several charts at once.

<p align="center">
  <img src="docs/Screenshot%202026-03-30%20at%2010.24.02.png" alt="Histogram dashboard for multiple courses" width="900">
</p>

### 3. Scatter comparison between courses

When two courses are selected, the dashboard can compare their grade relationship directly in a scatter plot.

<p align="center">
  <img src="docs/Screenshot%202026-03-30%20at%2010.24.47.png" alt="Scatter plot comparing two selected courses" width="900">
</p>

### 4. Alternative visualizations

The same selection can be rendered with different plot types, including swarm and joint plots, to highlight density and marginal distributions.

<p align="center">
  <img src="docs/Screenshot%202026-03-30%20at%2010.24.57.png" alt="Swarm plot for selected courses" width="440">
  <img src="docs/Screenshot%202026-03-30%20at%2010.25.09.png" alt="Joint plot with marginal distributions" width="440">
</p>

### 5. Grade prediction and evaluation

The prediction screen estimates a student's grade in a selected subject and reports the actual value, squared error, and model MSE on a deterministic holdout split.

<p align="center">
  <img src="docs/Screenshot%202026-03-30%20at%2010.28.53.png" alt="Grade prediction and evaluation screen" width="900">
</p>

## Project Structure

- `src/main/java/org/example/ui`: JavaFX views, controllers, and chart renderers
- `src/main/java/org/example/analytics`: filtering, statistics, and plot data preparation
- `src/main/java/org/example/ml`: random-forest training, prediction, and evaluation logic
- `src/main/java/org/example/data`: CSV-backed dataset loading and domain models
