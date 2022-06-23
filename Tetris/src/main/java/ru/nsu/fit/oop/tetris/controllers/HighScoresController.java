package ru.nsu.fit.oop.tetris.controllers;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ru.nsu.fit.oop.tetris.ModelBuilder;
import ru.nsu.fit.oop.tetris.SceneBuilder;
import ru.nsu.fit.oop.tetris.model.Model;
import ru.nsu.fit.oop.tetris.view.HighScores;

import java.io.IOException;

public class HighScoresController {
    private Model model = ModelBuilder.getModel();

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

    @FXML
    private GridPane pane;
    @FXML
    private Button back;

    @FXML
    private void handleBack(Event event) {
        toMenu();
    }

    private void toMenu() {
        Stage stage = (Stage) back.getScene().getWindow();
        try {
            stage.setScene(SceneBuilder.getMenu());
            model.toMenu();
        } catch (IOException e) {
            Platform.exit();
            e.printStackTrace();
        }
    }


    public void show() {
        ObservableList<Score> scores = FXCollections.observableArrayList();
        model.getHighScores().forEach((k, v) -> scores.add(new Score(k, v)));
        FXCollections.reverse(scores);
        TableView<Score> table = new TableView<>(scores);
        table.setBackground((new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY))));
        table.setPrefHeight(250);

        Label emptyTable = new Label("It's quite empty here...");
        emptyTable.setFont(Font.font("helvetica", FontWeight.BLACK, FontPosture.REGULAR, 20));
        emptyTable.setStyle("-fx-text-fill: mediumturquoise");
        table.setPlaceholder(emptyTable);

        TableColumn<Score, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setPrefWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        table.getColumns().add(nameColumn);
        TableColumn<Score, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setPrefWidth(200);
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        table.getColumns().add(scoreColumn);
        table.getColumns().forEach(column -> column.setStyle("-fx-background-color: mediumturquoise; -fx-font-size: 20pt; -fx-font-family: helvetica; -fx-alignment: center"));

        pane.getChildren().add(table);
    }
}
