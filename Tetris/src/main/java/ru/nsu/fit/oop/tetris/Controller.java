package ru.nsu.fit.oop.tetris;

import javafx.stage.Stage;

public class Controller {
    public void run(Stage stage) throws Exception {
        Model model = new Model();
        View view = new View(model, stage);
        model.start();
        stage.setOnCloseRequest(t -> model.pause());
    }
}
