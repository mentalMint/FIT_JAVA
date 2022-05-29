package ru.nsu.fit.oop.factory.model.supplies.details;

public class Detail implements IProduct {
    private final int id;

    public Detail(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
