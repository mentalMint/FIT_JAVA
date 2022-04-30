package ru.nsu.fit.oop.lab2.factory;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.lab2.factory.commands.Add;
import ru.nsu.fit.oop.lab2.factory.commands.Define;
import ru.nsu.fit.oop.lab2.factory.commands.Print;
import ru.nsu.fit.oop.lab2.factory.commands.Push;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.logging.Level;

import static java.lang.Double.parseDouble;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void execute() {
        List<Pair<String, List<String>>> commands = new ArrayList<>();
        commands.add(new Pair<>("DEFINE", new ArrayList(Arrays.asList("a", "2"))));
        commands.add(new Pair<>("PUSH", new ArrayList(Collections.singletonList("a"))));
        commands.add(new Pair<>("PUSH", new ArrayList(Collections.singletonList("3"))));
        commands.add(new Pair<>("+", null));
        commands.add(new Pair<>("PRINT", null));

        Calculator calculator = new Calculator();
        try {
            calculator.setProperties("config.txt");
        } catch (Exception e) {
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        calculator.execute(commands);

        assertEquals(5., parseDouble(out.toString()));
    }

    @Test
    void setProperties() {
        Calculator calculator = new Calculator();

        Exception exception = assertThrows(Exception.class, () -> {
            calculator.setProperties(null);
        });

        assertEquals(NullPointerException.class, exception.getClass());
    }
}