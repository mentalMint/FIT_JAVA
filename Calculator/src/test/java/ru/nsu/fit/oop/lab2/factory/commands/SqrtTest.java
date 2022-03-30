package ru.nsu.fit.oop.lab2.factory.commands;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
    }
}