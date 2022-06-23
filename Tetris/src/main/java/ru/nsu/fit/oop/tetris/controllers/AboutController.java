package ru.nsu.fit.oop.tetris.controllers;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ru.nsu.fit.oop.tetris.ModelBuilder;
import ru.nsu.fit.oop.tetris.SceneBuilder;
import ru.nsu.fit.oop.tetris.model.Model;

import java.io.IOException;

public class AboutController {
    private final Model model = ModelBuilder.getModel();

    @FXML
    private Button back;
    @FXML
    private Label text;

    @FXML
    private void handleBack(Event event) {
        toMenu();
    }

    private void toMenu() {
        Stage stage = (Stage) back.getScene().getWindow();
        try {
            stage.setScene(SceneBuilder.getMenu());
            model.toMenu();
        } catch (IOException e) {
            Platform.exit();
            e.printStackTrace();
        }
    }

    public void initialize() {
        text.setText("   Tetris, video game created by\n" +
                "Russian designer Alexey Pajitnov\n" +
                "in 1985 that allows players\n" +
                "to rotate falling blocks strategically\n" +
                "to clear levels. Pajitnov claimed\n" +
                "he created the name of the game\n" +
                "by combining the Greek prefix tetra,\n" +
                "which refers to the four squares\n" +
                "contained in each block, with\n" +
                " the word tennis.\n");
    }
}
