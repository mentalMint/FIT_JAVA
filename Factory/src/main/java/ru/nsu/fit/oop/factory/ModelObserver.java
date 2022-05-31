package ru.nsu.fit.oop.factory;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import ru.nsu.fit.oop.factory.model.Model;

import java.util.concurrent.Flow;

public class ModelObserver implements Flow.Subscriber<Boolean> {
    private final Model model;
    private final IntegerProperty bodyIdProperty;
    private final IntegerProperty engineIdProperty;
    private final IntegerProperty accessoryIdProperty;
    private final IntegerProperty bodyWarehouseWorkloadProperty;
    private final IntegerProperty engineWarehouseWorkloadProperty;
    private final IntegerProperty accessoryWarehouseWorkloadProperty;
    private final IntegerProperty autosNumberProperty;
    private final IntegerProperty autoWarehouseWorkloadProperty;

    public IntegerProperty getBodyWarehouseWorkloadProperty() {
        return bodyWarehouseWorkloadProperty;
    }

    public IntegerProperty getEngineWarehouseWorkloadProperty() {
        return engineWarehouseWorkloadProperty;
    }

    public IntegerProperty getAccessoryWarehouseWorkloadProperty() {
        return accessoryWarehouseWorkloadProperty;
    }

    public IntegerProperty getBodyIdProperty() {
        return bodyIdProperty;
    }

    public IntegerProperty getEngineIdProperty() {
        return engineIdProperty;
    }

    public IntegerProperty getAccessoryIdProperty() {
        return accessoryIdProperty;
    }

    public IntegerProperty getAutosNumberProperty() {
        return autosNumberProperty;
    }

    public IntegerProperty getAutoWarehouseWorkloadProperty() {
        return autoWarehouseWorkloadProperty;
    }

    public ModelObserver(Model model) {
        this.model = model;
        model.subscribe(this);
        bodyIdProperty = new SimpleIntegerProperty(0);
        engineIdProperty = new SimpleIntegerProperty(0);
        accessoryIdProperty = new SimpleIntegerProperty(0);
        bodyWarehouseWorkloadProperty = new SimpleIntegerProperty(0);
        engineWarehouseWorkloadProperty = new SimpleIntegerProperty(0);
        accessoryWarehouseWorkloadProperty = new SimpleIntegerProperty(0);
        autosNumberProperty = new SimpleIntegerProperty(0);
        autoWarehouseWorkloadProperty = new SimpleIntegerProperty(0);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
    }

    @Override
    public void onNext(Boolean item) {
        bodyIdProperty.set(model.getBodyId());
        engineIdProperty.set(model.getEngineId());
        accessoryIdProperty.set(model.getAccessoryId());
        bodyWarehouseWorkloadProperty.set(model.getBodyWarehouseWorkload());
        engineWarehouseWorkloadProperty.set(model.getEngineWarehouseWorkload());
        accessoryWarehouseWorkloadProperty.set(model.getAccessoryWarehouseWorkload());
        autosNumberProperty.set(model.getAutosNumber());
        autoWarehouseWorkloadProperty.set(model.getAutoWarehouseWorkload());
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }
}
