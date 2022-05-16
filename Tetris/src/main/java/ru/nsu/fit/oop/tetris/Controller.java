package ru.nsu.fit.oop.tetris;

import javafx.stage.Stage;
import ru.nsu.fit.oop.tetris.view.Game;
import ru.nsu.fit.oop.tetris.view.Score;
import ru.nsu.fit.oop.tetris.view.Menu;
import ru.nsu.fit.oop.tetris.view.Pause;

public class Controller {
    public void run(Stage stage) {
        Model model = new Model();
        Game game = new Game(model, stage);
        Menu menu = new Menu(model, stage);
        Pause pause = new Pause(model, stage);
        Score gameOver = new Score(model, stage);
        stage.setOnCloseRequest(t -> {
            model.exit();
        });
        stage.show();
    }
}

