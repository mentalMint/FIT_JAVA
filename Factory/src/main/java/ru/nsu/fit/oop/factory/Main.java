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
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("view.fxml"));
        Parent root = loader.load();
        stage.setTitle("MVC Example App");
        stage.setScene(new Scene(root, 720, 480));
        Controller controller = loader.getController();
        stage.setOnCloseRequest(windowEvent -> controller.exit());
        stage.show();
    }
}