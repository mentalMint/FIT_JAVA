package ru.nsu.fit.oop.chat.client;

import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Flow;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import ru.nsu.fit.oop.chat.client.model.Model;


public class Controller implements Flow.Subscriber<Boolean> {
    private Model model;

    @FXML
    private Button send;
    @FXML
    private TextField textField;
    @FXML
    private Pane pane;
    @FXML
    private Label message;

    private ArrayList<Label> messages;

    @FXML
    private void handleSend(Event event) {
        model.sendMessage(textField.getText());
        textField.setText("");
    }

    @FXML
    private void handleOnKeyReleased(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            String text = textField.getText();
            if (!text.equals("")) {
                model.sendMessage(text);
                textField.setText("");
            }
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
            label.setLayoutY(i * 50);
            messages.add(label);
            pane.getChildren().add(label);
        }

        model = new Model();
        model.subscribe(this);
        try {
            model.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        model.start();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onNext(Boolean item) {
        Platform.runLater(() -> {
            for (int i = 0; i < 9; i++) {
                messages.get(i).setText(messages.get(i + 1).getText());
            }
            messages.get(9).setText(model.getMessage());
        });
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}