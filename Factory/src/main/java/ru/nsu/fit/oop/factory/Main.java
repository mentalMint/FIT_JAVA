package ru.nsu.fit.oop.factory;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Controller controller = new Controller();
        controller.run(stage);
    }
}