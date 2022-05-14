package ru.nsu.fit.oop.tetris.View;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ru.nsu.fit.oop.tetris.Block;
import ru.nsu.fit.oop.tetris.Model;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.Flow;

public class Game implements Flow.Subscriber<Boolean> {
    private final Model model;
    private List<Block> blocks;
    private final int height = 720;
    private final int width = 480;
    private final Pane layout = new Pane();
    private final Stage stage;
    private final Scene scene = new Scene(layout, width, height);

    public Game(Model model, Stage stage) {
        this.model = model;
        model.subscribe(this);
        this.stage = stage;
        this.stage.setTitle("Tetris");

        layout.setBackground((new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY))));
        this.blocks = model.getField().getBlocks();

        double widthShift = (double) width / model.getField().getWidth();

        for (int i = 0; i < model.getField().getHeight(); i++) {
            for (int j = 0; j < model.getField().getWidth(); j++) {
                Rectangle r = new Rectangle();
                r.setY((i - 2) * widthShift);
                r.setX(j * widthShift);
                r.setWidth(widthShift);
                r.setHeight(widthShift);
                r.setArcWidth(20);
                r.setArcHeight(20);
                r.setFill(blocks.get(i * model.getField().getWidth() + j).color);
                layout.getChildren().add(r);
            }
        }
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.A) {
                try {
                    model.moveCurrentShapeLeft();
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            } else if (e.getCode() == KeyCode.D) {
                try {
                    model.moveCurrentShapeRight();
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            } else if (e.getCode() == KeyCode.SPACE) {
                try {
                    model.rotateCurrentShape();
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            } else if (e.getCode() == KeyCode.ESCAPE) {
                model.pause();
            } else if (e.getCode() == KeyCode.S) {
                model.increaseSpeed();
            }
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.S) {
                model.decreaseSpeed();
            }
        });
        scene.setRoot(layout);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onNext(Boolean item) {
        if (model.getGameState() == Model.GameState.GAME) {
            blocks = model.getField().getBlocks();

            for (int i = 0; i < model.getField().getHeight(); i++) {
                for (int j = 0; j < model.getField().getWidth(); j++) {
                    Rectangle r = (Rectangle) layout.getChildren().get(i * model.getField().getWidth() + j);
                    r.setFill(blocks.get(i * model.getField().getWidth() + j).color);
                }
            }
            scene.setRoot(layout);
            stage.setScene(scene);
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
