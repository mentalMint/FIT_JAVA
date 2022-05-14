package ru.nsu.fit.oop.tetris.View;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ru.nsu.fit.oop.tetris.Model;

public class About {
    public class Score {

        private SimpleStringProperty name;
        private SimpleIntegerProperty value;

        Score(String name, int value){
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleIntegerProperty(value);
        }

        public String getName(){ return name.get();}
        public void setName(String value){ name.set(value);}

        public int getValue(){ return value.get();}
        public void setValue(int value){ this.value.set(value);}
    }


    private final Model model;
    private final int height = 720;
    private final int width = 480;
    private final Pane layout = new Pane();
    private final Stage stage;
    private final Scene scene = new Scene(layout, width, height);
    private int buttonWidth = 200;
    private int buttonHeight = 70;

    public About(Model model, Stage stage) {
        this.model = model;
        this.stage = stage;
        this.stage.setTitle("Tetris");
        layout.setBackground(new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        Rectangle background = new Rectangle();
        background.setFill(Paint.valueOf( "#0aafa0"));
        background.setLayoutY(110);
        background.setLayoutX(30);
        background.setHeight(340);
        background.setWidth(410);

        layout.getChildren().add(background);

        Text text = new Text();
        text.setTranslateY(150);
        text.setTranslateX(50);
        text.setFont(Font.font("helvetica", FontWeight.BOLD, FontPosture.REGULAR, 22));
        text.setText("   Tetris, video game created by\nRussian designer Alexey Pajitnov\nin 1985 that allows players\nto rotate falling blocks strategically\nto clear levels. Pajitnov claimed\nhe created the name of the game\nby combining the Greek prefix tetra,\nwhich refers to the four squares\ncontained in each block, with\n the word tennis.\n");

        layout.getChildren().add(text);

        Button menuButton = ButtonCreator.createButton("Back", 500);
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

    public void show() {
        stage.setScene(scene);
    }
}
