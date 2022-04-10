package ru.nsu.fit.oop.tetris.shapes;

import ru.nsu.fit.oop.tetris.Block;
import ru.nsu.fit.oop.tetris.Color;

import java.util.ArrayList;

public class SquareShape extends Shape {

    public SquareShape(Color color, int x, int y) {
        super(color, x, y);
        super.blocks = new ArrayList<>();
        super.blocks.add(new Block(color, x, y));
        super.blocks.add(new Block(color, x + 1, y));
        super.blocks.add(new Block(color, x, y + 1));
        super.blocks.add(new Block(color, x + 1, y + 1));
    }

//    public void setBlocks(Color color) {
//        super.blocks = new ArrayList<>();
//        super.blocks.add(new Block(color, x, y));
//        super.blocks.add(new Block(color, x + 1, y));
//        super.blocks.add(new Block(color, x, y + 1));
//        super.blocks.add(new Block(color, x + 1, y + 1));
//    }

    public SquareShape(Shape shape) {
        super(shape);
    }
}
