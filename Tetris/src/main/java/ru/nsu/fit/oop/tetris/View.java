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
import java.util.List;
import java.util.concurrent.Flow;

public class View implements Flow.Subscriber<Boolean> {
    private Model model;
    private List<Block> blocks;
    private final int height = 720;
    private final int width = 480;
    private Pane layout = new Pane();
    private Stage stage = new Stage();
    private Scene scene = new Scene(layout, width, height);

    public View(Model model, Stage stage) throws Exception {
        this.model = model;
        model.subscribe(this);
//        model.addObserver(this);
        this.stage = stage;
        this.stage.setTitle("Tetris");

        InputStream background = View.class.getResourceAsStream("background.jpg");
        Image img = new Image(background);
        BackgroundImage bImg = new BackgroundImage(img,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(width, height, true, true, true, true));
        Background bGround = new Background(bImg);
        layout.setBackground(bGround);
        this.blocks = model.getField().getBlocks();

        double widthShift = (double) width / model.getField().getWidth();
        double heightShift = (double) height / model.getField().getHeight();

        for (int i = 0; i < model.getField().getHeight(); ++i) {
            for (int j = 0; j < model.getField().getWidth(); ++j) {
                Rectangle r = new Rectangle();
                r.setX(i * heightShift);
                r.setY(j * widthShift);
                r.setWidth(widthShift);
                r.setHeight(widthShift);
                r.setArcWidth(10);
                r.setArcHeight(10);
                if (blocks.get(i * model.getField().getWidth() + j).color != ru.nsu.fit.oop.tetris.Color.NO) {
                    r.setFill(Color.DARKGREEN);
                } else {
                    r.setFill(Color.TRANSPARENT);
                }
                layout.getChildren().add(r);
//                System.err.println(layout.getChildren().indexOf(r));
            }
        }

        scene.setRoot(layout);
        this.stage.setScene(scene);
        this.stage.show();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onNext(Boolean item) {
        blocks = model.getField().getBlocks();

        double widthShift = (double) width / model.getField().getWidth();
        double heightShift = (double) height / model.getField().getHeight();

        for (int i = 0; i < model.getField().getHeight(); ++i) {
            for (int j = 0; j < model.getField().getWidth(); ++j) {
                Rectangle r = new Rectangle();
                r.setX(j * widthShift);
                r.setY(i * widthShift);
                r.setWidth(widthShift);
                r.setHeight(widthShift);
                r.setArcWidth(10);
                r.setArcHeight(10);
                if (blocks.get(i * model.getField().getWidth() + j).color != ru.nsu.fit.oop.tetris.Color.NO) {
                    r.setFill(Color.DARKGREEN);
                } else {
                    r.setFill(Color.TRANSPARENT);
                }
//                layout.getChildren().set(i * model.getField().getWidth() + j, layout.getChildren().get(i * model.getField().getWidth() + j));
                layout.getChildren().set(i * model.getField().getWidth() + j, r);
            }
        }

        scene.setRoot(layout);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
