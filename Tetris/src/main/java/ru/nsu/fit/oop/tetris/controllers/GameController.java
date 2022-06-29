package ru.nsu.fit.oop.tetris.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ru.nsu.fit.oop.tetris.model.Block;
import ru.nsu.fit.oop.tetris.model.Model;
import ru.nsu.fit.oop.tetris.ModelBuilder;
import ru.nsu.fit.oop.tetris.SceneBuilder;
import ru.nsu.fit.oop.tetris.exceptions.ShapeCreationException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Flow;

public class GameController implements Flow.Subscriber<Object> {
    private final Model model = ModelBuilder.getModel();
    private MediaPlayer player;

    @FXML
    private GridPane field;
    @FXML
    private Label score;

    @FXML
    private void handleOnKeyReleased(KeyEvent event) {
        if (event.getCode().equals(KeyCode.S)) {
            model.decreaseSpeed();
        }
    }

    @FXML
    private void handleOnKeyPressed(KeyEvent event) {
        try {
            if (event.getCode().equals(KeyCode.A)) {
                model.moveCurrentShapeLeft();
            } else if (event.getCode().equals(KeyCode.D)) {
                model.moveCurrentShapeRight();
            } else if (event.getCode().equals(KeyCode.S)) {
                model.increaseSpeed();
            } else if (event.getCode() == KeyCode.SPACE) {
                try {
                    model.rotateCurrentShape();
                } catch (ShapeCreationException ex) {
                    ex.printStackTrace();
                }
            } else if (event.getCode() == KeyCode.ESCAPE) {
                model.pause();
                Stage stage = (Stage) field.getScene().getWindow();
                stage.setScene(SceneBuilder.getPause());
            }
        } catch (ShapeCreationException | IOException e) {
            Platform.exit();
            e.printStackTrace();
        }
    }

    public void initialize() {
        model.subscribe(this);
        player = new MediaPlayer(new Media(Objects.requireNonNull(getClass().getResource("TetrisBeat-box.mp3")).toExternalForm()));
        player.setCycleCount(MediaPlayer.INDEFINITE);
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
                rectangle.setFill(Color.TRANSPARENT);
                field.getChildren().add(rectangle);
            }
        }
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onNext(Object item) {
        Platform.runLater(() -> {
            if (model.getGameState() == Model.GameState.SCORE) {
                Stage stage = (Stage) field.getScene().getWindow();
                if (stage != null) {
                    try {
                        stage.setScene(SceneBuilder.getScore());
                    } catch (IOException e) {
                        Platform.exit();
                        e.printStackTrace();
                    }
                }
            } else {
                score.setText(Integer.toString(model.getScore()));

                if (model.getGameState() == Model.GameState.GAME) {
                    player.play();
                    List<Block> blocks = model.getField().getBlocks();
                    for (int i = 0; i < model.getField().getHeight() - 2; i++) {
                        for (int j = 0; j < model.getField().getWidth(); j++) {
                            int index = (i + 2) * model.getField().getWidth() + j;
                            Rectangle rectangle = (Rectangle) field.getChildren().get(i * model.getField().getWidth() + j);
                            rectangle.setFill(blocks.get(index).color);
                        }
                    }
                } else if (model.getGameState() == Model.GameState.PAUSE) {
                    player.pause();
                } else {
                    player.stop();
                }
            }
        });
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
