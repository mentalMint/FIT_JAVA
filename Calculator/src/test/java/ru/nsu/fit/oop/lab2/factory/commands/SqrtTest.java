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
        Calculator.ExecutionContext context = new Calculator.ExecutionContext();
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        stack.add(2.);
        context.stack = stack;
        Sqrt sqrt = new Sqrt();

        sqrt.execute(null, context);

        assertEquals(Math.sqrt(2), context.stack.peek());
    }

    @Test
    void failToExecuteWithNullArguments() {
        Calculator.ExecutionContext context = new Calculator.ExecutionContext();
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        context.stack = stack;
        Sqrt sqrt = new Sqrt();

        Exception exception = assertThrows(Exception.class, () -> {
            sqrt.execute(null, context);
        });

        String expectedMessage = "Sqrt. Less then 1 number in the stack. Too few for Sqrt.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void failToExecuteWithWrongArguments() {
        Calculator.ExecutionContext context = new Calculator.ExecutionContext();
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        context.stack = stack;
        stack.add(-2.);
        Sqrt sqrt = new Sqrt();

        Exception exception = assertThrows(Exception.class, () -> {
            sqrt.execute(null, context);
        });

        String expectedMessage = "Argument is less then zero.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}