package ru.nsu.fit.oop.lab2.factory.commands;

import ru.nsu.fit.oop.lab2.factory.Calculator;
import ru.nsu.fit.oop.lab2.factory.Command;

import java.util.List;

public class Define implements Command {
    @Override
    public void execute(List<String> arguments, Calculator.ExecutionContext context) {
        if (arguments.size() != 2) {
            throw new IllegalArgumentException("Define. Two arguments were expected.");
        }

        String argument0 = arguments.get(0);

        try
        {
            Double.parseDouble(argument0);
            throw new IllegalArgumentException("Define. The first argument can't be a number.");
        }
        catch(NumberFormatException e1)
        {
            String argument1 = arguments.get(1);
            try
            {
                Double.parseDouble(argument1);
            }
            catch(NumberFormatException e2)
            {
                throw new IllegalArgumentException("Define. The second argument has to be a number.");
            }
            context.parameters.put(argument0, Double.valueOf(argument1));
        }
    }
}
