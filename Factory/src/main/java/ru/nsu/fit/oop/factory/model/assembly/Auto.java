package ru.nsu.fit.oop.factory.model.assembly;

import ru.nsu.fit.oop.factory.model.supplies.details.IProduct;

import java.util.List;

public class Auto implements IProduct {
    private final int id;
    private IProduct accessory;
    private IProduct body;
    private IProduct engine;

    public Auto(int id) {
        this.id = id;
    }

    public void setAccessory(IProduct accessory) {
        this.accessory = accessory;
    }

    public void setBody(IProduct body) {
        this.body = body;
    }

    public void setEngine(IProduct engine) {
        this.engine = engine;
    }

    public IProduct getAccessory() {
        return accessory;
    }

    public IProduct getBody() {
        return body;
    }

    public IProduct getEngine() {
        return engine;
    }

    @Override
    public int getId() {
        return id;
    }
}
