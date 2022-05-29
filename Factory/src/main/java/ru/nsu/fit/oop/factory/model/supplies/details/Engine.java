package ru.nsu.fit.oop.factory.model.supplies.details;

public class Engine extends Detail {
    private String type = "Engine";

    public String getType() {
        return type;
    }

    public Engine(int id) {
        super(id);
    }
}