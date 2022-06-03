package ru.nsu.fit.oop.factory;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.LoadException;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import ru.nsu.fit.oop.factory.model.Model;

public class Controller {
    private final Model model = new Model();

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
        double delay = 1000;
        if (speed > 0) {
            delay = (1000 / speed);
        }
        return (long) delay;
    }

    public void exit() {
        model.stop();
    }

    public Controller() throws LoadException {
    }

    public void initialize() {
        model.start();
        ModelObserver view = new ModelObserver(model);
        totalBodies.textProperty().bind(view.getBodyIdProperty().asString());
        totalEngines.textProperty().bind(view.getEngineIdProperty().asString());
        totalAccessories.textProperty().bind(view.getAccessoryIdProperty().asString());
        bodyWarehouseWorkload.textProperty().bind(view.getBodyWarehouseWorkloadProperty().asString());
        engineWarehouseWorkload.textProperty().bind(view.getEngineWarehouseWorkloadProperty().asString());
        accessoryWarehouseWorkload.textProperty().bind(view.getAccessoryWarehouseWorkloadProperty().asString());
        totalAutos.textProperty().bind(view.getAutosNumberProperty().asString());
        autoWarehouseWorkload.textProperty().bind(view.getAutoWarehouseWorkloadProperty().asString());

    }
}