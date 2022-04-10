package ru.nsu.fit.oop.tetris;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import ru.nsu.fit.oop.tetris.observer.*;

import java.io.InputStream;

public class View implements Observer {
    private Model model;
    private final int height = 720;
    private final int width = 480;

    public View(Model model, Stage stage) {
        this.model = model;
        model.addObserver(this);
        stage.setTitle("Tetris");
        Pane layout = new Pane();

        InputStream  background = View.class.getResourceAsStream("background.jpg");
        Image img = new Image(background);
        BackgroundImage bImg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(width, height, true, true, true, true));
        Background bGround = new Background(bImg);
        layout.setBackground(bGround);


        double widthShift = (double) width / model.getField().getWidth();
        double heightShift = (double) height / model.getField().getHeight();

        for (int i = 0; i < model.getField().getWidth(); ++i) {
            for (int j = 0; j < model.getField().getHeight(); ++j) {
                Rectangle r = new Rectangle();
                r.setX(i * widthShift);
                r.setY(j * heightShift);
                r.setWidth(widthShift);
                r.setHeight(widthShift);
                r.setArcWidth(10);
                r.setArcHeight(10);
                r.setFill(Color.DARKGREEN);
                layout.getChildren().add(r);
            }
        }

        Scene scene = new Scene(layout, width, height);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void handleEvent() {

    }
}
