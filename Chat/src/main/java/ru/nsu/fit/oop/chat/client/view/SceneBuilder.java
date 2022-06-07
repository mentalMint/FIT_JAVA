package ru.nsu.fit.oop.chat.client.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import ru.nsu.fit.oop.chat.ClientMain;
import ru.nsu.fit.oop.chat.client.Controller;

import java.io.IOException;

public class SceneBuilder {
//    private final Scene registrationScene;
//    private final Scene chatScene;

    public SceneBuilder() throws IOException {
//        FXMLLoader loader = new FXMLLoader(SceneBuilder.class.getResource("chat.fxml"));
//        chatScene = new Scene(loader.load());
//        loader = new FXMLLoader(SceneBuilder.class.getResource("registration.fxml"));
//
////        loader.setLocation(SceneBuilder.class.getResource("registration.fxml"));
//        registrationScene = new Scene(loader.load());
    }

    public static Scene getChatScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneBuilder.class.getResource("chat.fxml"));
        return new Scene(loader.load());
    }

    public static Scene getRegistrationScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneBuilder.class.getResource("registration.fxml"));
        return new Scene(loader.load());
    }
}
