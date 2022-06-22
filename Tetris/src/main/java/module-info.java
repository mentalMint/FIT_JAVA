module ru.nsu.fit.oop.tetris {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens ru.nsu.fit.oop.tetris to javafx.fxml;
    exports ru.nsu.fit.oop.tetris;
    exports ru.nsu.fit.oop.tetris.view;
    exports ru.nsu.fit.oop.tetris.model.shapes;
    exports ru.nsu.fit.oop.tetris.exceptions;
    exports ru.nsu.fit.oop.tetris.controllers;
    opens ru.nsu.fit.oop.tetris.view to javafx.fxml;
    opens ru.nsu.fit.oop.tetris.controllers to javafx.fxml;
    opens ru.nsu.fit.oop.tetris.model to javafx.fxml;
    exports ru.nsu.fit.oop.tetris.model;

}