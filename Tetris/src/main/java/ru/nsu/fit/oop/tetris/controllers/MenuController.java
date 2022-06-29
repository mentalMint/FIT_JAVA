package ru.nsu.fit.oop.tetris.controllers;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ru.nsu.fit.oop.tetris.model.Model;
import ru.nsu.fit.oop.tetris.ModelBuilder;
import ru.nsu.fit.oop.tetris.SceneBuilder;
import ru.nsu.fit.oop.tetris.exceptions.ClassesRegistrationException;
import ru.nsu.fit.oop.tetris.exceptions.ShapeCreationException;

import java.io.IOException;

public class MenuController {
    private final Model model = ModelBuilder.getModel();

    @FXML
    private Button start;
    @FXML
    private Button highScores;
    @FXML
    private Button about;

    @FXML
    private void handleStart(Event event) {
        Stage stage = (Stage) start.getScene().getWindow();
        try {
            stage.setScene(SceneBuilder.getGame());
            model.start();
            stage.setOnCloseRequest(e -> model.exit());
        } catch (IOException | ClassesRegistrationException | ShapeCreationException e) {
            Platform.exit();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleHighScores(Event event) {
        Stage stage = (Stage) highScores.getScene().getWindow();
        try {
            stage.setScene(SceneBuilder.getHighScores());
            SceneBuilder.getHighScoresLoader().<HighScoresController>getController().show();
        } catch (IOException e) {
            Platform.exit();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAbout(Event event) {
        Stage stage = (Stage) about.getScene().getWindow();
        try {
            stage.setScene(SceneBuilder.getAbout());
        } catch (IOException e) {
            Platform.exit();
            e.printStackTrace();
        }
    }

}
