package ru.nsu.fit.oop.lab2.factory.commands;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.lab2.exceptions.NoSuchParameterException;
import ru.nsu.fit.oop.lab2.factory.Calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class DefineTest {
    @Test
    void execute() throws NoSuchParameterException {
        Calculator.ExecutionContext context = new Calculator.ExecutionContext();
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        context.stack = stack;
        List<String> arguments = new ArrayList<>();
        arguments.add("a");
        arguments.add("4");
        Define define = new Define();
        List<String> pushArguments = new ArrayList<>();
        pushArguments.add("a");
        Push push = new Push();

        define.execute(arguments, context);
        push.execute(pushArguments, context);

        assertEquals(4., context.stack.peek());
    }

    @Test
    void failToExecuteWithNullArguments() {
        Calculator.ExecutionContext context = new Calculator.ExecutionContext();
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        context.stack = stack;
        Define define = new Define();

        Exception exception = assertThrows(Exception.class, () -> {
            define.execute(null, context);
        });

        String expectedMessage = "arguments is null";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void failToExecuteWithWrongArguments() {
        Calculator.ExecutionContext context = new Calculator.ExecutionContext();
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        context.stack = stack;
        Define define = new Define();
        List<String> arguments = new ArrayList<>();
        arguments.add("a");

        Exception exception = assertThrows(Exception.class, () -> {
            define.execute(arguments, context);
        });

        String expectedMessage = "Define. Two arguments were expected.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}