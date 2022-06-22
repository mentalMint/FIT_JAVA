package ru.nsu.fit.oop.chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nsu.fit.oop.chat.client.Controller;

import java.io.IOException;

public class ClientMain2 extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        FXMLLoader loader = new FXMLLoader(ClientMain.class.getResource("registration.fxml"));
        try {
            stage.show();
            Parent root = loader.load();
            stage.setTitle("Chat 2");
            stage.setScene(new Scene(root, 480, 720));
            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
