package ru.nsu.fit.oop.tetris;

import ru.nsu.fit.oop.tetris.model.Model;

public class ModelBuilder {
    private static Model model;

    public static Model getModel() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }
}
