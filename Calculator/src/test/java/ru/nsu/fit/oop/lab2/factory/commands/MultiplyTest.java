package ru.nsu.fit.oop.lab2.factory.commands;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.lab2.exceptions.EmptyStackException;
import ru.nsu.fit.oop.lab2.factory.Calculator;

import java.util.HashMap;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class MultiplyTest {
    @Test
    void execute() throws EmptyStackException {
        Calculator.ExecutionContext context = mock(Calculator.ExecutionContext.class);
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        stack.add(2.);
        stack.add(3.);
        context.stack = stack;
        Multiply multiply = new Multiply();

        multiply.execute(null, context);

        assertEquals(6., context.stack.peek());


        context.parameters = new HashMap<>();
        stack = new Stack<>();
        context.stack = stack;

        Exception exception = assertThrows(Exception.class, () -> {
            multiply.execute(null, context);
        });

        String expectedMessage = "Multiply. Less then 2 numbers in the stack. Too few for Multiply.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}