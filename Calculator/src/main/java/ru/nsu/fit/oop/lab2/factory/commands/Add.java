package ru.nsu.fit.oop.lab2.factory.commands;

import ru.nsu.fit.oop.lab2.exceptions.EmptyStackException;
import ru.nsu.fit.oop.lab2.factory.Calculator;
import ru.nsu.fit.oop.lab2.factory.Command;

import java.util.List;

public class Add implements Command {
    @Override
    public void execute(List<String> arguments, Calculator.ExecutionContext context) throws EmptyStackException {
        if (context.stack.size() < 2) {
            throw new EmptyStackException("Add. Less then 2 numbers in the stack. Too few for Add.");
        }
        Double a = context.stack.pop();
        Double b = context.stack.pop();
        context.stack.push((a + b));
    }
}
