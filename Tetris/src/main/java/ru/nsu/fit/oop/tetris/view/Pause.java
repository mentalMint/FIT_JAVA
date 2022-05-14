package ru.nsu.fit.oop.tetris.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ru.nsu.fit.oop.tetris.Model;

import java.util.concurrent.Flow;

public class Pause implements Flow.Subscriber<Boolean> {
    private final Model model;
    private final int height = 720;
    private final int width = 480;
    private final Pane layout = new Pane();
    private final Stage stage;
    private final Scene scene = new Scene(layout, width, height);

    public Pause(Model model, Stage stage) {
        this.model = model;
        model.subscribe(this);
        this.stage = stage;

        layout.setBackground((new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY))));

        Button resumeButton = ButtonCreator.createButton("Resume", 400);
        EventHandler<ActionEvent> resumeEvent = e -> {
            try {
                model.resume();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        resumeButton.setOnAction(resumeEvent);
        layout.getChildren().add(resumeButton);

        Button menuButton = ButtonCreator.createButton("Menu", 300);
        EventHandler<ActionEvent> menuEvent = e -> {
            try {
                model.toMenu();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        menuButton.setOnAction(menuEvent);
        layout.getChildren().add(menuButton);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onNext(Boolean item) {
        if (model.getGameState() == Model.GameState.PAUSE) {
            this.stage.setScene(scene);
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
