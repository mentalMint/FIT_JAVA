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

import java.io.IOException;

public class HighScoresController {
    private final Model model = ModelBuilder.getModel();
    private ObservableList<Score> scores;
    @FXML
    private Button back;
    @FXML
    private TableView<Score> table;
    @FXML
    private TableColumn<Score, String> nameColumn;
    @FXML
    private TableColumn<Score, Integer> scoreColumn;

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

    public void initialize() {
        scores = FXCollections.observableArrayList();
        model.getHighScores().forEach((k, v) -> scores.add(new Score(k, v)));
        FXCollections.reverse(scores);
        table.setItems(scores);
        table.setBackground((new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY))));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
    }

    public void show() {
        scores.clear();
        model.getHighScores().forEach((k, v) -> scores.add(new Score(k, v)));
        FXCollections.reverse(scores);
        table.setItems(scores);
    }

    public class Score {
        private final SimpleStringProperty name;
        private final SimpleIntegerProperty value;

        Score(String name, int value) {
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleIntegerProperty(value);
        }

        public String getName() {
            return name.get();
        }

        public void setName(String value) {
            name.set(value);
        }

        public int getValue() {
            return value.get();
        }

        public void setValue(int value) {
            this.value.set(value);
        }
    }
}
