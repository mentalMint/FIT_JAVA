package ru.nsu.fit.oop.lab2.factory.commands;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.lab2.exceptions.EmptyStackException;
import ru.nsu.fit.oop.lab2.factory.Calculator;

import java.util.HashMap;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SqrtTest {
    @Test
    void execute() throws EmptyStackException {
        Calculator.ExecutionContext context = mock(Calculator.ExecutionContext.class);
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        stack.add(2.);
        context.stack = stack;
        Sqrt sqrt = new Sqrt();

        sqrt.execute(null, context);

        assertEquals(Math.sqrt(2), context.stack.peek());


        context.parameters = new HashMap<>();
        stack = new Stack<>();
        context.stack = stack;

        Exception exception = assertThrows(Exception.class, () -> {
            sqrt.execute(null, context);
        });

        String expectedMessage = "Sqrt. Less then 1 number in the stack. Too few for Sqrt.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));


        context.parameters = new HashMap<>();
        stack = new Stack<>();
        context.stack = stack;
        stack.add(-2.);

        exception = assertThrows(Exception.class, () -> {
            sqrt.execute(null, context);
        });

        expectedMessage = "Argument is less then zero.";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}