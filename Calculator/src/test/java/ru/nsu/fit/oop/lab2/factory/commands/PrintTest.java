package ru.nsu.fit.oop.lab2.factory.commands;

import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.lab2.exceptions.EmptyStackException;
import ru.nsu.fit.oop.lab2.factory.Calculator;

import java.util.HashMap;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class PrintTest {
    @Test
    void execute() throws EmptyStackException {
        Calculator.ExecutionContext context = mock(Calculator.ExecutionContext.class);
        context.parameters = new HashMap<>();
        Stack<Double> stack = new Stack<>();
        stack.add(2.);
        context.stack = stack;
        Print print = new Print();
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        print.execute(null, context);

        assertEquals(2., new Double(out.toString()));


        context.parameters = new HashMap<>();
        stack = new Stack<>();
        context.stack = stack;

        Exception exception = assertThrows(Exception.class, () -> {
            print.execute(null, context);
        });

        String expectedMessage = "Print. Less then 1 number in the stack. Too few for Print.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}