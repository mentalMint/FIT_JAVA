package ru.nsu.fit.oop.lab2.factory.commands;

import ru.nsu.fit.oop.lab2.factory.Calculator;
import ru.nsu.fit.oop.lab2.factory.Interpreter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @org.junit.jupiter.api.Test
    void execute() {
        List<String> commands = new ArrayList<>();
        commands.add("DEFINE a 2");
        commands.add("#comment");
        commands.add("PUSH a");
        commands.add("PUSH 3");
        commands.add("+");
        commands.add("PRINT");
        Interpreter calculator = new Calculator();
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        calculator.execute(commands);

        assertEquals(5., new Double(out.toString()));
    }
}