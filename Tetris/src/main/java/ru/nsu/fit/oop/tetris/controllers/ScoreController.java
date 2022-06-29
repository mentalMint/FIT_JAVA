package ru.nsu.fit.oop.tetris.controllers;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ru.nsu.fit.oop.tetris.ModelBuilder;
import ru.nsu.fit.oop.tetris.SceneBuilder;
import ru.nsu.fit.oop.tetris.model.Model;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Flow;

public class ScoreController implements Flow.Subscriber<Object> {
    private final Model model = ModelBuilder.getModel();
    @FXML
    private Button submit;
    @FXML
    private Button skip;
    @FXML
    private TextField nameField;
    @FXML
    private Label score;

    @FXML
    private void handleSubmit(Event event) {
        String name = nameField.getText();
        if (name.trim().length() > 0) {
            model.addScore(name);
            toMenu();
        }
        nameField.setText("");
    }

    @FXML
    private void handleOnKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            handleSubmit(null);
        }
    }

    @FXML
    private void handleSkip(Event event) {
        toMenu();
    }

    private void toMenu() {
        Stage stage = (Stage) skip.getScene().getWindow();
        try {
            stage.setScene(SceneBuilder.getMenu());
            model.toMenu();
        } catch (IOException e) {
            Platform.exit();
            e.printStackTrace();
        }
    }

    public void initialize() {
        model.subscribe(this);
        score.setText("Your score " +model.getScore());
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onNext(Object item) {
        if (model.getGameState() == Model.GameState.SCORE) {
            Platform.runLater(() -> score.setText("Your score " +model.getScore()));
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
