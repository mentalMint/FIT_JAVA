package ru.nsu.fit.oop.tetris.shapes;

import javafx.scene.paint.Color;
import ru.nsu.fit.oop.tetris.Block;

import java.util.ArrayList;
import java.util.List;

public abstract class Shape implements IShape {
    public List<Block> blocks = new ArrayList<>();
    public int x;
    public int y;
    Color color;
    TurningSide turningSide = TurningSide.UP;

    public Shape(Color color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public Shape(Shape shape) {
        color = shape.color;
        x = shape.x;
        y = shape.y;
        turningSide = shape.turningSide;
//        blocks = new ArrayList<>(shape.blocks);
        for (Block block : shape.blocks) {
            blocks.add(new Block((block)));
        }
    }

    public enum TurningSide {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }
}
