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

class DefineTest {
    @Test
    void execute() throws NoSuchParameterException {
        Calculator.ExecutionContext context = mock(Calculator.ExecutionContext.class);
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
}