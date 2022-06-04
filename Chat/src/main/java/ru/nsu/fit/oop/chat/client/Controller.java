package ru.nsu.fit.oop.chat.client;

import javafx.application.Platform;

import java.io.IOException;
import java.util.concurrent.Flow;

import ru.nsu.fit.oop.chat.client.model.Model;


public class Controller implements Flow.Subscriber<Boolean> {
    private final Model model;

    public void exit() {
//        model.stop();
    }

    public Controller() throws IOException {
        model = new Model();
    }

    public void initialize() {
//        model.start();
        model.subscribe(this);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onNext(Boolean item) {
        Platform.runLater(() -> {

        });
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}