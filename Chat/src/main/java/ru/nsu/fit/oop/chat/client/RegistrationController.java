package ru.nsu.fit.oop.chat.client;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.nsu.fit.oop.chat.ClientMain;
import ru.nsu.fit.oop.chat.client.view.SceneBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class RegistrationController {
    @FXML
    private Button login;
    @FXML
    private TextField nameField;

    @FXML
    private void handleLogin(Event event) {
        String name = nameField.getText();
        if (!Objects.equals(name, "")) {
            Stage stage;
            Parent root;
            Controller controller;
            stage = (Stage) login.getScene().getWindow();
            try {
                FXMLLoader loader = new FXMLLoader(ClientMain.class.getResource("chat.fxml"));
                root = loader.load();
                Scene scene = new Scene(root);
                controller = loader.getController();
                controller.login(name);
                stage.setOnCloseRequest(windowEvent -> {
                    try {
                        controller.exit();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Platform.exit();
                    }
                });
                stage.setScene(scene);
            } catch (IOException e) {
                Platform.exit();
            }
        }
    }
}
