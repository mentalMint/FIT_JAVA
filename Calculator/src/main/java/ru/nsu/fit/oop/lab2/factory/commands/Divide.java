package ru.nsu.fit.oop.lab2.factory.commands;

import ru.nsu.fit.oop.lab2.exceptions.EmptyStackException;
import ru.nsu.fit.oop.lab2.factory.Calculator;
import ru.nsu.fit.oop.lab2.factory.Command;

import java.util.List;

public class Divide implements Command {
    @Override
    public void execute(List<String> arguments, Calculator.ExecutionContext context) throws EmptyStackException {
        if (context.stack.size() < 2) {
            throw new EmptyStackException("Divide. Less then 2 numbers in the stack. Too few for Divide.");
        }
        Double a = context.stack.pop();
        Double b = context.stack.pop();
        if (b == 0) {
            throw new IllegalArgumentException("Division by zero");
        }
        context.stack.push((a / b));
    }
}
