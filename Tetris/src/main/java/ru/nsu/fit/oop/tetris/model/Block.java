package ru.nsu.fit.oop.tetris.model;

import javafx.scene.paint.Color;

public class Block {
    public Color color;
    public int x;
    public int y;

    public Block(Color color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public Block(Block block) {
        color = block.color;
        x = block.x;
        y = block.y;
    }
}
