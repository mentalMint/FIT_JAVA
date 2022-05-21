package ru.nsu.fit.oop.tetris.exceptions;

public class ShapeCreationException extends ReflectiveOperationException{
    public ShapeCreationException() {
        super();
    }

    public ShapeCreationException(String s) {
        super(s);
    }

    public ShapeCreationException(Throwable cause) {
        super(cause);
    }
}
