package ru.nsu.fit.oop.lab2.factory.commands;

import ru.nsu.fit.oop.lab2.exceptions.EmptyStackException;
import ru.nsu.fit.oop.lab2.factory.Calculator;
import ru.nsu.fit.oop.lab2.factory.Command;

import java.util.List;

public class Pop implements Command {
    @Override
    public void execute(List<String> arguments, Calculator.ExecutionContext context) throws EmptyStackException {
        if (context.stack.size() < 1) {
            throw new EmptyStackException("Pop. Less then 2 numbers in the stack. Too few for Pop.");
        }
        context.stack.pop();
    }
}
