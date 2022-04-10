package ru.nsu.fit.oop.tetris.shapes;

import ru.nsu.fit.oop.tetris.Block;
import ru.nsu.fit.oop.tetris.Color;

import java.util.List;

public class Shape implements IShape {
    public List<Block> blocks = null;
    public int x = 0;
    public int y = 0;
    Color color;

    public Shape(Color color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public Shape(Shape shape) {
        this.color = shape.color;
        this.x = shape.x;
        this.y = shape.y;
        this.blocks = shape.blocks;
    }
}
