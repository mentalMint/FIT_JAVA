package ru.nsu.fit.oop.factory.model.supplies.details;

public class Accessory extends Detail {
    private String type = "Accessories";

    public String getType() {
        return type;
    }

    public Accessory(int id) {
        super(id);
    }
}
