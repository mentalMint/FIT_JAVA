package ru.nsu.fit.oop.tetris.shapes;

import javafx.scene.paint.Color;
import ru.nsu.fit.oop.tetris.Block;

public class BarShape extends Shape {
    public BarShape(Color color, int x, int y) {
        super(color, x, y);
        blocks.add(new Block(color,-1,-1));
        blocks.add(new Block(color, 0,-1));
        blocks.add(new Block(color, 1,-1));
        blocks.add(new Block(color, 2,-1));
    }

    public BarShape(BarShape shape) {
        super(shape);
    }
}
