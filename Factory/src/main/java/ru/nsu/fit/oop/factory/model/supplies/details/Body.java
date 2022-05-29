package ru.nsu.fit.oop.factory.model.supplies.details;

public class Body extends Detail {
    private String type = "Body";

    public String getType() {
        return type;
    }

    public Body(int id) {
        super(id);
    }
}
