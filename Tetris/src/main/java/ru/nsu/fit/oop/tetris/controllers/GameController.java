package ru.nsu.fit.oop.tetris.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.nsu.fit.oop.tetris.Model;
import ru.nsu.fit.oop.tetris.ModelBuilder;
import ru.nsu.fit.oop.tetris.exceptions.ShapeCreationException;

import java.util.concurrent.Flow;

public class GameController implements Flow.Subscriber<Object> {
    private final Model model = ModelBuilder.getModel();

    @FXML
    private void handleOnKeyReleased(KeyEvent event) {
        try {
            if (event.getCode().equals(KeyCode.A)) {
                model.moveCurrentShapeLeft();
            } else if (event.getCode().equals(KeyCode.D)) {
                model.moveCurrentShapeRight();
            } else if (event.getCode().equals(KeyCode.S)) {
                model.decreaseSpeed();
            }
        } catch (ShapeCreationException e) {
            Platform.exit();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOnKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.S)) {
            model.increaseSpeed();
        }
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onNext(Object item) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
