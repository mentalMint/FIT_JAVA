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
            stage.setWidth(480);
            stage.setHeight(720);
            stage.setResizable(false);
            stage.setScene(SceneBuilder.getMenu());
            stage.show();
        } catch (IOException e) {
            Platform.exit();
            e.printStackTrace();
        }
    }
}
