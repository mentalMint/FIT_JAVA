package ru.nsu.fit.oop.factory;

import javafx.fxml.LoadException;
import javafx.stage.Stage;
import ru.nsu.fit.oop.factory.model.Model;

public class Controller {
    public void run(Stage stage) {
        try {
            Model model = new Model();
            model.start();
            stage.setOnCloseRequest(t -> {
                model.stop();
            });
            stage.show();
        } catch (LoadException e) {
            e.printStackTrace();
        }


    }
}