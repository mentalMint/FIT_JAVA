package ru.nsu.fit.oop.lab2.factory.commands;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.lab2.exceptions.NoSuchParameterException;
import ru.nsu.fit.oop.lab2.factory.Calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PushTest {
    @Test
    void execute() throws NoSuchParameterException {
        Calculator.ExecutionContext context = mock(Calculator.ExecutionContext.class);
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        context.stack = stack;
        List<String> arguments = new ArrayList<>();
        arguments.add("3");
        Push push = new Push();

        push.execute(arguments, context);

        assertEquals(3., context.stack.peek());


        context.parameters = new HashMap<>();
        stack = new Stack<>();
        context.stack = stack;
        arguments = new ArrayList<>();
        arguments.add("a");
        arguments.add("3");

        List<String> finalArguments = arguments;
        Exception exception = assertThrows(Exception.class, () -> {
            push.execute(finalArguments, context);
        });

        String expectedMessage = "Push. One argument was expected.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}