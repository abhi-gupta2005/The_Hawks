package com.example;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Map;

public class EnhancedAILearningPlatformGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("AI-Powered Learning Platform");

        // Initialize database
        DatabaseHelper.initializeDatabase();

        // Fetch content and progress from the database
        Map<String, Map<String, String>> offlineContent = DatabaseHelper.fetchContent();
        Map<String, Map<String, Integer>> studentProgress = DatabaseHelper.fetchProgress();

        // Create a GridPane layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Add components to the grid
        Label welcomeLabel = new Label("Welcome to the AI-Powered Learning Platform!");
        GridPane.setConstraints(welcomeLabel, 0, 0, 2, 1);

        // Display recommended modules
        Map<String, String> recommendations = DatabaseHelper.recommendModule(studentProgress);
        Label recommendationsLabel = new Label("Recommended Modules for You:");
        GridPane.setConstraints(recommendationsLabel, 0, 1);

        StringBuilder recommendedModules = new StringBuilder();
        for (Map.Entry<String, String> entry : recommendations.entrySet()) {
            recommendedModules.append(entry.getKey().toUpperCase()).append(": ").append(entry.getValue()).append("\n");
        }
        TextArea recommendationsArea = new TextArea(recommendedModules.toString());
        recommendationsArea.setEditable(false);
        GridPane.setConstraints(recommendationsArea, 0, 2, 2, 1);

        // Subject and topic input
        Label subjectLabel = new Label("Enter a subject (math, science, language):");
        GridPane.setConstraints(subjectLabel, 0, 3);
        TextField subjectInput = new TextField();
        GridPane.setConstraints(subjectInput, 1, 3);

        Label topicLabel = new Label("Enter a topic:");
        GridPane.setConstraints(topicLabel, 0, 4);
        TextField topicInput = new TextField();
        GridPane.setConstraints(topicInput, 1, 4);

        // Language selection
        Label languageLabel = new Label("Choose a language (english, hindi, tamil):");
        GridPane.setConstraints(languageLabel, 0, 5);
        TextField languageInput = new TextField();
        GridPane.setConstraints(languageInput, 1, 5);

        // Button to fetch content
        Button fetchContentButton = new Button("Fetch Content");
        GridPane.setConstraints(fetchContentButton, 0, 6);

        // Display content and translated content
        TextArea contentArea = new TextArea();
        contentArea.setEditable(false);
        GridPane.setConstraints(contentArea, 0, 7, 2, 1);

        // Display points
        Label pointsLabel = new Label();
        GridPane.setConstraints(pointsLabel, 0, 8, 2, 1);

        // Progress Bars
        ProgressBar mathProgressBar = new ProgressBar(0.8); // Example progress for math
        ProgressBar scienceProgressBar = new ProgressBar(0.6); // Example progress for science
        ProgressBar languageProgressBar = new ProgressBar(0.4); // Example progress for language

        VBox progressBars = new VBox(10, new Label("Math Progress:"), mathProgressBar,
                new Label("Science Progress:"), scienceProgressBar,
                new Label("Language Progress:"), languageProgressBar);
        GridPane.setConstraints(progressBars, 0, 9, 2, 1);

        // Bar Chart for Progress Visualization
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> progressChart = new BarChart<>(xAxis, yAxis);
        progressChart.setTitle("Subject Progress");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Math", 80));
        series.getData().add(new XYChart.Data<>("Science", 60));
        series.getData().add(new XYChart.Data<>("Language", 40));

        progressChart.getData().add(series);
        GridPane.setConstraints(progressChart, 0, 10, 2, 1);

        // Set action for the fetch content button
        fetchContentButton.setOnAction(e -> {
            String subject = subjectInput.getText().toLowerCase();
            String topic = topicInput.getText().toLowerCase();
            String language = languageInput.getText().toLowerCase();

            String content = DatabaseHelper.accessOfflineContent(subject, topic);
            String translatedContent = DatabaseHelper.translateContent(content, language);

            contentArea.setText("Content:\n" + content + "\n\nTranslated Content:\n" + translatedContent);

            int points = DatabaseHelper.calculatePoints(studentProgress);
            pointsLabel.setText("Your Total Learning Points: " + points);

            // Add animation
            FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), contentArea);
            fadeTransition.setFromValue(0);
            fadeTransition.setToValue(1);
            fadeTransition.play();
        });

        // Add all components to the grid
        grid.getChildren().addAll(welcomeLabel, recommendationsLabel, recommendationsArea,
                subjectLabel, subjectInput, topicLabel, topicInput, languageLabel, languageInput,
                fetchContentButton, contentArea, pointsLabel, progressBars, progressChart);

        // Set the scene and show the stage
        Scene scene = new Scene(grid, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
