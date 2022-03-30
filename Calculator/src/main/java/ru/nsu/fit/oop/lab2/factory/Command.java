package ru.nsu.fit.oop.lab2.factory;

import ru.nsu.fit.oop.lab2.exceptions.EmptyStackException;
import ru.nsu.fit.oop.lab2.exceptions.NoSuchParameterException;

import java.util.List;

public interface Command {
    void execute(List<String> arguments, Calculator.ExecutionContext context) throws EmptyStackException, NoSuchParameterException;
}
