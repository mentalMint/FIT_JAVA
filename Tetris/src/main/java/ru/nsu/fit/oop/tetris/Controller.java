package ru.nsu.fit.oop.tetris;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Controller {
    public void run(Stage stage) throws Exception {
        Model model = new Model();
        View view = new View(model, stage);
        model.start();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                model.pause();
            }
        });
    }
}
