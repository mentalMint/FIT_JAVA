package ru.nsu.fit.oop.factory;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.LoadException;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import ru.nsu.fit.oop.factory.model.Model;

import java.util.concurrent.Flow;

public class Controller implements Flow.Subscriber<Boolean> {
    private final Model model;

    @FXML
    private Label totalBodies;
    @FXML
    private Label totalEngines;
    @FXML
    private Label totalAccessories;
    @FXML
    private Label bodyWarehouseWorkload;
    @FXML
    private Label engineWarehouseWorkload;
    @FXML
    private Label accessoryWarehouseWorkload;
    @FXML
    private Slider bodySupplierSpeed;
    @FXML
    private Slider engineSupplierSpeed;
    @FXML
    private Slider accessorySupplierSpeed;
    @FXML
    private Slider dealerSpeed;
    @FXML
    private Label totalAutos;
    @FXML
    private Label autoWarehouseWorkload;

    @FXML
    private void setBodySupplierSpeed(Event event) {
        long delay = calcDelay(bodySupplierSpeed.getValue());
        model.setBodySupplierDelay(delay);
        event.consume();
    }

    @FXML
    private void setEngineSupplierSpeed(Event event) {
        long delay = calcDelay(engineSupplierSpeed.getValue());
        model.setEngineSupplierDelay(delay);
        event.consume();
    }

    @FXML
    private void setAccessorySupplierSpeed(Event event) {
        long delay = calcDelay(accessorySupplierSpeed.getValue());
        model.setAccessorySupplierDelay(delay);
        event.consume();
    }

    @FXML
    private void setDealerSpeed(Event event) {
        long delay = calcDelay(dealerSpeed.getValue());
        model.setDealerDelay(delay);
        event.consume();
    }


    private long calcDelay(double speed) {
        System.err.println(speed);

        double delay = 1000;
        if (speed > 0) {
            delay = (1000 / speed);
        }
        return (long) delay;
    }

    public void exit() {
        model.stop();
    }

    public Controller() throws ModelInitializingException {
        try {
            model = new Model();
        } catch (LoadException e) {
            throw new ModelInitializingException();
        }
    }

    public void initialize() {
        model.start();
        model.subscribe(this);
        totalBodies.textProperty().set(Integer.toString(0));
        totalEngines.textProperty().set(Integer.toString(0));
        totalAccessories.textProperty().set(Integer.toString(0));
        totalAutos.textProperty().set(Integer.toString(0));
        bodyWarehouseWorkload.textProperty().set(Integer.toString(0));
        engineWarehouseWorkload.textProperty().set(Integer.toString(0));
        accessoryWarehouseWorkload.textProperty().set(Integer.toString(0));
        autoWarehouseWorkload.textProperty().set(Integer.toString(0));
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {

    }

    @Override
    public void onNext(Boolean item) {
        Platform.runLater(() -> {
            totalBodies.textProperty().set(Integer.toString(model.getBodyId()));
            totalEngines.textProperty().set(Integer.toString(model.getEngineId()));
            totalAccessories.textProperty().set(Integer.toString(model.getAccessoryId()));
            totalAutos.textProperty().set(Integer.toString(model.getAutosNumber()));
            bodyWarehouseWorkload.textProperty().set(Integer.toString(model.getBodyWarehouseWorkload()));
            engineWarehouseWorkload.textProperty().set(Integer.toString(model.getEngineWarehouseWorkload()));
            accessoryWarehouseWorkload.textProperty().set(Integer.toString(model.getAccessoryWarehouseWorkload()));
            autoWarehouseWorkload.textProperty().set(Integer.toString(model.getAutoWarehouseWorkload()));
        });
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}