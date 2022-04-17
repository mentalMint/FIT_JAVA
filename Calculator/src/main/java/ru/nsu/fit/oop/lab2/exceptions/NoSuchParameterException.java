package ru.nsu.fit.oop.lab2.exceptions;

public class NoSuchParameterException extends Exception{
    public NoSuchParameterException() {
        super();
    }

    public NoSuchParameterException(String message) {
        super(message);
    }
}
