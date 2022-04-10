module ru.nsu.fit.oop.tetris {
    requires javafx.controls;
    requires javafx.fxml;

    opens ru.nsu.fit.oop.tetris to javafx.fxml;
    exports ru.nsu.fit.oop.tetris;
}