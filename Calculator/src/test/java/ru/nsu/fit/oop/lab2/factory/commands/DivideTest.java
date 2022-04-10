package ru.nsu.fit.oop.lab2.factory.commands;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.lab2.exceptions.EmptyStackException;
import ru.nsu.fit.oop.lab2.factory.Calculator;

import java.util.HashMap;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DivideTest {
    @Test
    void execute() throws EmptyStackException {
        Calculator.ExecutionContext context = mock(Calculator.ExecutionContext.class);
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        stack.add(2.);
        stack.add(3.);
        context.stack = stack;
        Divide divide = new Divide();

        divide.execute(null, context);

        assertEquals(3. / 2., context.stack.peek());


        context.parameters = new HashMap<>();
        stack = new Stack<>();
        context.stack = stack;

        Exception exception = assertThrows(Exception.class, () -> {
            divide.execute(null, context);
        });

        String expectedMessage = "Divide. Less then 2 numbers in the stack. Too few for Divide.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));


        context.parameters = new HashMap<>();
        stack = new Stack<>();
        context.stack = stack;
        stack.add(0.);
        stack.add(3.);

        exception = assertThrows(Exception.class, () -> {
            divide.execute(null, context);
        });

        expectedMessage = "Division by zero";
        actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}