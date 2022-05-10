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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ru.nsu.fit.oop.tetris.Block;
import ru.nsu.fit.oop.tetris.Model;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.Flow;

public class Menu implements Flow.Subscriber<Boolean> {
    private final Model model;
    private List<Block> blocks;
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
        Button startButton = new Button("Start");
        int buttonWidth = 100;
        int buttonHeight = 70;
        startButton.setTranslateX((float) width / 2 - (float) buttonWidth / 2);
        startButton.setTranslateY(300);
        startButton.setPrefSize(buttonWidth, buttonHeight);
        startButton.setStyle("-fx-background-color: #0aafa0");
        startButton.setFont(Font.font("helvetica", FontWeight.BLACK, FontPosture.REGULAR, 25));
        EventHandler<ActionEvent> event = e -> {
            try {
                model.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        startButton.setOnAction(event);
        layout.getChildren().add(startButton);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                try {
                    model.start();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

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
