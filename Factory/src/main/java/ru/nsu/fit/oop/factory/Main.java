package ru.nsu.fit.oop.factory;

import javafx.application.Application;
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
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("view.fxml"));
        try {
            Parent root = loader.load();
            stage.setTitle("Factory");
            stage.setScene(new Scene(root, 720, 480));
            Controller controller = loader.getController();
            stage.setOnCloseRequest(windowEvent -> controller.exit());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}