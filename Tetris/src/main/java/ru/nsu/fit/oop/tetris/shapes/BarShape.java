package ru.nsu.fit.oop.tetris.shapes;

import javafx.scene.paint.Color;
import ru.nsu.fit.oop.tetris.Block;

public class BarShape extends Shape {
    public BarShape(Color color, int x, int y) {
        super(color, x, y);
        blocks.add(new Block(color, 0, 0));
        blocks.add(new Block(color, 1, 0));
        blocks.add(new Block(color, 2, 0));
        blocks.add(new Block(color, 3, 0));
    }

    public BarShape(BarShape shape) {
        super(shape);
    }

    @Override
    public void rotate() {
        switch (turningSide) {
            case UP -> {
                blocks.get(0).x = 0;
                blocks.get(0).y = 0;
                blocks.get(1).x = 0;
                blocks.get(1).y = 1;
                blocks.get(2).x = 0;
                blocks.get(2).y = 2;
                blocks.get(3).x = 0;
                blocks.get(3).y = 3;
                x += 2;
                y--;
                turningSide = TurningSide.RIGHT;
            }
            case RIGHT -> {
                blocks.get(0).x = 0;
                blocks.get(0).y = 0;
                blocks.get(1).x = 1;
                blocks.get(1).y = 0;
                blocks.get(2).x = 2;
                blocks.get(2).y = 0;
                blocks.get(3).x = 3;
                blocks.get(3).y = 0;
                x -= 2;
                y += 2;
                turningSide = TurningSide.DOWN;
            }
            case DOWN -> {
                blocks.get(0).x = 0;
                blocks.get(0).y = 0;
                blocks.get(1).x = 0;
                blocks.get(1).y = 1;
                blocks.get(2).x = 0;
                blocks.get(2).y = 2;
                blocks.get(3).x = 0;
                blocks.get(3).y = 3;
                x++;
                y -= 2;
                turningSide = TurningSide.LEFT;
            }
            case LEFT -> {
                blocks.get(0).x = 0;
                blocks.get(0).y = 0;
                blocks.get(1).x = 1;
                blocks.get(1).y = 0;
                blocks.get(2).x = 2;
                blocks.get(2).y = 0;
                blocks.get(3).x = 3;
                blocks.get(3).y = 0;
                x--;
                y++;
                turningSide = TurningSide.UP;
            }
        }
    }
}
