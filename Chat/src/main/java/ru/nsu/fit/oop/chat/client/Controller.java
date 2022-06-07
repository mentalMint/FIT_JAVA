package ru.nsu.fit.oop.chat.client;

import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Flow;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ru.nsu.fit.oop.chat.client.model.Model;
import ru.nsu.fit.oop.chat.packets.Response;


public class Controller implements Flow.Subscriber<Response> {
    private Model model;

    @FXML
    private Button send;
    @FXML
    private TextField textField;
    @FXML
    private Pane pane;
    @FXML
    private Label message;
    @FXML
    private Label members;
    @FXML
    private Button membersButton;
    @FXML
    private Image buttonImage;
//    @FXML
//    private Button login;

    private ArrayList<Label> messages;

//    @FXML
//    private void handleLogin(Event event) {
//        Stage stage;
//        Pane root;
//
//        stage = (Stage) login.getScene().getWindow();
//        try {
//            root = FXMLLoader.load(getClass().getResource("chat.fxml"));
//            stage = (Stage) login.getScene().getWindow();
//            Scene scene = new Scene(root);
//            stage.setScene(scene);
//        } catch (IOException e) {
//            Platform.exit();
//        }
//    }

    @FXML
    private void handleInfo(Event event) {
        if (members.isVisible()) {
            members.setVisible(false);
        } else {
            try {
                model.sendMembersRequest();
            } catch (IOException e) {
                Platform.exit();
            }
        }
    }

    @FXML
    private void handleSend(Event event) {
        sendMessage();
    }

    @FXML
    private void handleOnKeyReleased(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            sendMessage();
        }
    }

    private void sendMessage() {
        String text = textField.getText();
        if (!text.equals("")) {
            try {
                model.sendMessage(text);
            } catch (IOException e) {
                e.printStackTrace();
                Platform.exit();
            }
            textField.setText("");
        }
    }

    @FXML
    private void handleOnKeyReleasedVbox(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ESCAPE)) {
            try {
                exit();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.exit();
        }
    }

    public void exit() throws IOException {
        model.stop();
    }

    public Controller() {
    }

    public void initialize() {
        messages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Label label = new Label("");
            label.setLayoutY(i * 50 + 100);
            messages.add(label);
            pane.getChildren().add(label);
        }
        members.setVisible(false);

//        buttonImage = new Image(Objects.requireNonNull(Controller.class.getResourceAsStream("members.png")));
//        ImageView imageView = new ImageView(buttonImage);
//        imageView.setFitHeight(membersButton.getPrefHeight());
//        imageView.setFitWidth(membersButton.getPrefWidth());
//        imageView.set
//        membersButton.setGraphic(imageView);

        model = new Model();
        model.subscribe(this);
    }

    public void login(String name) throws IOException {
        model.start(name);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onNext(Response response) {
        Platform.runLater(() -> {
            if (response.getType() == Response.Type.MEMBERS) {
                members.setVisible(true);
                members.setText(response.getBody());
            } else {
                for (int i = 0; i < 9; i++) {
                    messages.get(i).setText(messages.get(i + 1).getText());
                }
                messages.get(9).setText(formText(response));
            }
        });
    }

    private String formText(Response response) {
//        StringBuilder text = new StringBuilder();
        if (response.getType() == Response.Type.USER_CONNECTED) {
            return response.getInitiatorName() + " joined this chat";
        } else if (response.getType() == Response.Type.MESSAGE) {
            return response.getInitiatorName() + ": " + response.getBody();
        } else if (response.getType() == Response.Type.USER_DISCONNECTED) {
            return response.getInitiatorName() + " disconnected";
        } else if (response.getType() == Response.Type.MEMBERS) {
            return "Members:\n" + response.getBody();
        }
        return "";
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}