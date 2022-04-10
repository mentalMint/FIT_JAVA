package ru.nsu.fit.oop.lab2.factory.commands;

import ru.nsu.fit.oop.lab2.exceptions.NoSuchParameterException;
import ru.nsu.fit.oop.lab2.factory.Calculator;
import ru.nsu.fit.oop.lab2.factory.Command;

import java.util.List;

public class Push implements Command {
    @Override
    public void execute(List<String> arguments, Calculator.ExecutionContext context) throws NoSuchParameterException {
        if (arguments == null) {
            throw new NullPointerException("arguments is null");
        }

        if (arguments.size() != 1) {
            throw new IllegalArgumentException("Push. One argument was expected.");
        }

        String argument = arguments.get(0);
        Double number;
        try
        {
            Double.parseDouble(argument);
            number = new Double(argument);
        }
        catch(NumberFormatException e)
        {
            number = context.parameters.get(argument);
            if (number == null) {
                throw new NoSuchParameterException("Push. Parameter isn't defined.");
            }
        }
        context.stack.push(number);
    }
}
