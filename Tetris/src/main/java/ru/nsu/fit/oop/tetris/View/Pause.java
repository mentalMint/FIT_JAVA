package ru.nsu.fit.oop.tetris.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ru.nsu.fit.oop.tetris.Block;
import ru.nsu.fit.oop.tetris.Model;

import java.util.List;
import java.util.concurrent.Flow;

public class Pause implements Flow.Subscriber<Boolean> {
    private final Model model;
    private List<Block> blocks;
    private final int height = 720;
    private final int width = 480;
    private final Pane layout = new Pane();
    private final Stage stage;
    private final Scene scene = new Scene(layout, width, height);

    public Pause(Model model, Stage stage) {
        this.model = model;
        model.subscribe(this);
        this.stage = stage;

//        InputStream background = View.class.getResourceAsStream("background.jpg");
//        if (background == null) {
//            System.err.println("No background");
//        } else {
//            Image img = new Image(background);
//            BackgroundImage bImg = new BackgroundImage(img,
//                    BackgroundRepeat.NO_REPEAT,
//                    BackgroundRepeat.NO_REPEAT,
//                    BackgroundPosition.DEFAULT,
//                    new BackgroundSize(width, height, true, true, true, true));
//            Background bGround = new Background(bImg);
//            layout.setBackground(bGround);
//        }
        layout.setBackground((new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY))));
        Button resumeButton = new Button("Resume");
        int buttonWidth = 100;
        int buttonHeight = 70;
        resumeButton.setTranslateX((float) width / 2 - (float) buttonWidth / 2);
        resumeButton.setTranslateY(400);
        resumeButton.setPrefSize(buttonWidth, buttonHeight);
        resumeButton.setStyle("-fx-background-color: #0aafa0");
        resumeButton.setFont(Font.font("helvetica", FontWeight.BLACK, FontPosture.REGULAR, 20));
        EventHandler<ActionEvent> resumeEvent = e -> {
            try {
                model.resume();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        resumeButton.setOnAction(resumeEvent);
        layout.getChildren().add(resumeButton);

        Button menuButton = new Button("Menu");
        menuButton.setTranslateX((float) width / 2 - (float) buttonWidth / 2);
        menuButton.setTranslateY(300);
        menuButton.setPrefSize(buttonWidth, buttonHeight);
        menuButton.setStyle("-fx-background-color: #0aafa0");
        menuButton.setFont(Font.font("helvetica", FontWeight.BLACK, FontPosture.REGULAR, 20));
        EventHandler<ActionEvent> menuEvent = e -> {
            try {
                model.stop();
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
