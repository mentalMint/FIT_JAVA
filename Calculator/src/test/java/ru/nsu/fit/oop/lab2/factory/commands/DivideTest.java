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
        Calculator.ExecutionContext context = new Calculator.ExecutionContext();
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        stack.add(2.);
        stack.add(3.);
        context.stack = stack;
        Divide divide = new Divide();

        divide.execute(null, context);

        assertEquals(3. / 2., context.stack.peek());
    }

    @Test
    void failToExecuteWithNullArguments() {
        Calculator.ExecutionContext context = new Calculator.ExecutionContext();
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        context.stack = stack;
        Divide divide = new Divide();

        Exception exception = assertThrows(Exception.class, () -> {
            divide.execute(null, context);
        });

        String expectedMessage = "Divide. Less then 2 numbers in the stack. Too few for Divide.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void failToExecuteWithWrongArguments() {
        Calculator.ExecutionContext context = new Calculator.ExecutionContext();
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        stack = new Stack<>();
        stack.add(0.);
        stack.add(3.);
        context.stack = stack;
        Divide divide = new Divide();

        Exception exception = assertThrows(Exception.class, () -> {
            divide.execute(null, context);
        });

        String expectedMessage = "Division by zero";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}