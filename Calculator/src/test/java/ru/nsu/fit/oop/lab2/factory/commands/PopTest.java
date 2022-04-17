package ru.nsu.fit.oop.lab2.factory.commands;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.lab2.exceptions.EmptyStackException;
import ru.nsu.fit.oop.lab2.factory.Calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class PopTest {
    @Test
    void execute() throws EmptyStackException {
        Calculator.ExecutionContext context = new Calculator.ExecutionContext();
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        context.stack = stack;
        stack.push(21.);
        stack.push(12.);
        List<String> arguments = new ArrayList<>();
        arguments.add("3");
        Pop pop = new Pop();

        pop.execute(arguments, context);

        assertEquals(21., context.stack.peek());
    }

    @Test
    void failToExecuteWithNullArguments() {
        Calculator.ExecutionContext context = new Calculator.ExecutionContext();
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        context.stack = stack;
        Pop pop = new Pop();

        Exception exception = assertThrows(Exception.class, () -> {
            pop.execute(null, context);
        });

        String expectedMessage = "Pop. Less then 1 number in the stack. Too few for Pop.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}