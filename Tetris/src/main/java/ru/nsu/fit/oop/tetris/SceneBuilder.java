package ru.nsu.fit.oop.tetris;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

public class SceneBuilder {
    private static Scene game;
    private static Scene menu;
    private static Scene score;
    private static Scene pause;
    private static Scene highScores;
    private static Scene about;
    private static FXMLLoader highScoresLoader;

    public static FXMLLoader getHighScoresLoader() {
        return highScoresLoader;
    }

    public static Scene getAbout() throws IOException {
        if (about == null) {
            FXMLLoader loader = new FXMLLoader(SceneBuilder.class.getResource("view/about.fxml"));
            Parent root = loader.load();
            about = new Scene(root);
        }
        return about;
    }

    public static Scene getHighScores() throws IOException {
        if (highScores == null) {
            highScoresLoader = new FXMLLoader(SceneBuilder.class.getResource("view/high-scores.fxml"));
            Parent root = highScoresLoader.load();
            highScores = new Scene(root);
        }
        return highScores;
    }

    public static Scene getMenu() throws IOException {
        if (menu == null) {
            FXMLLoader loader = new FXMLLoader(SceneBuilder.class.getResource("view/menu.fxml"));
            Parent root = loader.load();
            menu = new Scene(root);
        }
        return menu;
    }

    public static Scene getPause() throws IOException {
        if (pause == null) {
            FXMLLoader loader = new FXMLLoader(SceneBuilder.class.getResource("view/pause.fxml"));
            Parent root = loader.load();
            pause = new Scene(root);
        }
        return pause;
    }

    public static Scene getGame() throws IOException {
        if (game == null) {
            FXMLLoader loader = new FXMLLoader(SceneBuilder.class.getResource("view/game.fxml"));
            Parent root = loader.load();
            game = new Scene(root);
            game.getRoot().requestFocus();
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
