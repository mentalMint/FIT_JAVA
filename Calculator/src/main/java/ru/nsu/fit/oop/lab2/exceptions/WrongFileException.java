package ru.nsu.fit.oop.lab2.exceptions;

public class WrongFileException extends Exception {
    public WrongFileException() {
        super();
    }

    public WrongFileException(String message) {
        super(message);
    }
}
