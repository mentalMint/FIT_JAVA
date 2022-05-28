package ru.nsu.fit.oop.factory.model.supplies.products;

import ru.nsu.fit.oop.factory.model.supplies.warehouses.IProduct;

import java.util.List;

public class Engine implements IDetail {
    private final int id;

    public Engine(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public List<IProduct> getDetails() {
        return null;
    }
}