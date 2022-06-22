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

public class Menu implements Flow.Subscriber<Boolean> {
    private final Model model;
    private final int height = 720;
    private final int width = 480;
    private final Pane layout = new Pane();
    private final Stage stage;
    private final Scene scene = new Scene(layout, width, height);

    public Menu(Model model, Stage stage) {
        this.model = model;
        model.subscribe(this);
        this.stage = stage;
        this.stage.setTitle("Tetris");

        layout.setBackground((new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY))));
        Button startButton = ButtonCreator.createButton("Start", 300);
        EventHandler<ActionEvent> event = e -> {
            try {
                model.start();
            } catch (Exception ex) {
                ex.printStackTrace();
                stage.close();
            }
        };

        startButton.setOnAction(event);
        layout.getChildren().add(startButton);

        HighScores highScores = new HighScores(model, stage);
        Button highScoresButton = ButtonCreator.createButton("High scores", 400);
        EventHandler<ActionEvent> highScoresEvent = e -> {
            highScores.show();
        };

        highScoresButton.setOnAction(highScoresEvent);
        layout.getChildren().add(highScoresButton);

        About about = new About(model, stage);
        Button aboutButton = ButtonCreator.createButton("About", 500);
        EventHandler<ActionEvent> aboutEvent = e -> {
            about.show();
        };

        aboutButton.setOnAction(aboutEvent);
        layout.getChildren().add(aboutButton);
        this.stage.setScene(scene);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
    }

    @Override
    public void onNext(Boolean item) {
        if (model.getGameState() == Model.GameState.MENU) {
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
