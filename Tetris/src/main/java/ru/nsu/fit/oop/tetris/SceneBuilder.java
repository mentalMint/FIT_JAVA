package ru.nsu.fit.oop.tetris;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class SceneBuilder {
    private static Scene game;
    private static Scene menu;
    private static Scene score;
    private static Scene highScores;
    private static Scene about;

    public static Scene getMenu() throws IOException {
        if (menu == null) {
            FXMLLoader loader = new FXMLLoader(SceneBuilder.class.getResource("view/menu.fxml"));
            Parent root = loader.load();
            menu = new Scene(root);
        }
        return menu;
    }

    public static Scene getGame() throws IOException {
        if (game == null) {
            FXMLLoader loader = new FXMLLoader(SceneBuilder.class.getResource("view/game.fxml"));
            Parent root = loader.load();
            game = new Scene(root);
        }
        return game;
    }

    public static Scene getScore() throws IOException {
        if (score == null) {
            FXMLLoader loader = new FXMLLoader(SceneBuilder.class.getResource("view/score.fxml"));
            Parent root = loader.load();
            score = new Scene(root);
        }
        return score;
    }
}
