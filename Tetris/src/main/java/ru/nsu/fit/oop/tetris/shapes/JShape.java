package ru.nsu.fit.oop.tetris.shapes;

import javafx.scene.paint.Color;
import ru.nsu.fit.oop.tetris.Block;

public class JShape extends Shape {
    public JShape(Color color, int x, int y) {
        super(color, x, y);
        blocks.add(new Block(color,-1,0));
        blocks.add(new Block(color, 1,1));
        blocks.add(new Block(color, 0,0));
        blocks.add(new Block(color, 1,0));
    }

    public JShape(JShape shape) {
        super(shape);
    }
}
