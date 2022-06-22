package ru.nsu.fit.oop.tetris;

public class ModelBuilder {
    private static Model model;

    public static Model getModel() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }
}
