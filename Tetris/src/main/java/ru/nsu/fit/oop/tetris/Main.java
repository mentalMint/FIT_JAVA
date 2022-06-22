package ru.nsu.fit.oop.tetris;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {
            stage.setTitle("Tetris");
            stage.setResizable(false);
            stage.setWidth(480);
            stage.setHeight(720);
            stage.show();
            stage.setScene(SceneBuilder.getMenu());
        } catch (IOException e) {
            Platform.exit();
            e.printStackTrace();
        }
    }
}
