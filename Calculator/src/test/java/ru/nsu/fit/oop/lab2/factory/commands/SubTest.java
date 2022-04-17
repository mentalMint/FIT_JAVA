package ru.nsu.fit.oop.lab2.factory.commands;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.lab2.exceptions.EmptyStackException;
import ru.nsu.fit.oop.lab2.factory.Calculator;

import java.util.HashMap;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class SubTest {
    @Test
    void execute() throws EmptyStackException {
        Calculator.ExecutionContext context = new Calculator.ExecutionContext();
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        stack.add(2.);
        stack.add(3.);
        context.stack = stack;
        Sub sub = new Sub();

        sub.execute(null, context);

        assertEquals(1., context.stack.peek());
    }

    @Test
    void failToExecuteWithNullArguments() {
        Calculator.ExecutionContext context = new Calculator.ExecutionContext();
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        context.stack = stack;
        Sub sub = new Sub();

        Exception exception = assertThrows(Exception.class, () -> {
            sub.execute(null, context);
        });

        String expectedMessage = "Sub. Less then 2 numbers in the stack. Too few for Sub.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}