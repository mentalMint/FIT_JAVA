package ru.nsu.fit.oop.tetris.shapes;

import javafx.scene.paint.Color;
import ru.nsu.fit.oop.tetris.Block;

public class TShape extends Shape {
    public TShape(Color color, int x, int y) {
        super(color, x, y);
        blocks.add(new Block(color,-1,0));
        blocks.add(new Block(color, 0,0));
        blocks.add(new Block(color, 1,0));
        blocks.add(new Block(color, 0,1));
    }

    public TShape(TShape shape) {
        super(shape);
    }
}
