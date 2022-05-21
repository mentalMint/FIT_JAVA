package ru.nsu.fit.oop.tetris.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ru.nsu.fit.oop.tetris.Model;

public class HighScores {
    public class Score {

        private final SimpleStringProperty name;
        private final SimpleIntegerProperty value;

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

    public HighScores(Model model, Stage stage) {
        this.model = model;
        this.stage = stage;
        this.stage.setTitle("Tetris");
        layout.setBackground(new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

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
        ObservableList<Score> scores = FXCollections.observableArrayList();
        model.getHighScores().forEach((k, v) -> scores.add(new Score(k, v)));
        FXCollections.reverse(scores);
        TableView<Score> table = new TableView<>(scores);
        table.setBackground((new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY))));
        table.setPrefHeight(250);
        table.setTranslateY(200);
        table.setTranslateX((float) width / 2 - 200);
        Label emptyTable = new Label("It's quite empty here...");
        emptyTable.setFont(Font.font("helvetica", FontWeight.BLACK, FontPosture.REGULAR, 20));
        emptyTable.setTextFill(Paint.valueOf("#34CFBE"));
        table.setPlaceholder(emptyTable);

        TableColumn<Score, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setPrefWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().add(nameColumn);
        TableColumn<Score, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setPrefWidth(200);
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        table.getColumns().add(scoreColumn);
        table.getColumns().forEach(column -> column.setStyle("-fx-background-color: #34CFBE; -fx-font-size: 20pt; -fx-font-family: helvetica; -fx-alignment: center"));


        layout.getChildren().add(table);
        stage.setScene(scene);
    }
}
