package ru.nsu.fit.oop.tetris.model.shapes;

import javafx.scene.paint.Color;
import ru.nsu.fit.oop.tetris.model.Block;

import java.util.ArrayList;
import java.util.List;

public abstract class Shape implements IShape {
    public List<Block> blocks = new ArrayList<>();
    public int x;
    public int y;
    Color color;

    public Shape(Color color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public Shape(Shape shape) {
        color = shape.color;
        x = shape.x;
        y = shape.y;

        for (Block block : shape.blocks) {
            blocks.add(new Block((block)));
        }
    }

    @Override
    public void rotate() {
        for (Block block : blocks) {
            int x = block.x;
            int y = block.y;
            block.x = -y ;
            block.y = x;
        }
    }
}
