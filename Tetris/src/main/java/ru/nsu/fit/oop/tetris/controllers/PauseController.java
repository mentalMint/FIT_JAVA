package ru.nsu.fit.oop.tetris.controllers;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ru.nsu.fit.oop.tetris.model.Block;
import ru.nsu.fit.oop.tetris.model.Model;
import ru.nsu.fit.oop.tetris.ModelBuilder;
import ru.nsu.fit.oop.tetris.SceneBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class PauseController implements Flow.Subscriber<Object> {
    private final Model model = ModelBuilder.getModel();

    @FXML
    private GridPane field;
    @FXML
    private Button resume;
    @FXML
    private Button menu;

    @FXML
    private void handleResume(Event event) {
        Stage stage = (Stage) resume.getScene().getWindow();
        try {
            stage.setScene(SceneBuilder.getGame());
            model.resume();
        } catch (IOException e) {
            Platform.exit();
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenu(Event event) {
        Stage stage = (Stage) menu.getScene().getWindow();
        try {
            stage.setScene(SceneBuilder.getMenu());
            model.toMenu();
        } catch (IOException e) {
            Platform.exit();
        }
    }

    public void initialize() {
        model.subscribe(this);
        field.setEffect(new GaussianBlur(12));
        double shift = field.getPrefWidth() / model.getField().getWidth();
        for (int i = 2; i < model.getField().getHeight(); i++) {
            for (int j = 0; j < model.getField().getWidth(); j++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setTranslateY(i * shift);
                rectangle.setTranslateX(j * shift);
                rectangle.setHeight(shift);
                rectangle.setWidth(48);
                rectangle.setArcWidth(20);
                rectangle.setArcHeight(20);
                rectangle.setFill(model.getField().getBlocks().get(i * model.getField().getWidth() + j).color);
                field.getChildren().add(rectangle);
            }
        }
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onNext(Object item) {
        if (model.getGameState() == Model.GameState.PAUSE) {
            Platform.runLater(() -> {
                List<Block> blocks = model.getField().getBlocks();
                for (int i = 0; i < model.getField().getHeight() - 2; i++) {
                    for (int j = 0; j < model.getField().getWidth(); j++) {
                        int index = (i + 2) * model.getField().getWidth() + j;
                        Rectangle rectangle = (Rectangle) field.getChildren().get(i * model.getField().getWidth() + j);
                        rectangle.setFill(blocks.get(index).color);
                    }
                }
            });
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
