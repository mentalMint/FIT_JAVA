package ru.nsu.fit.oop.tetris.model.shapes;

import ru.nsu.fit.oop.tetris.model.Block;

import java.util.List;

public interface IShape {
    List<Block> blocks = null;
    void rotate();
}