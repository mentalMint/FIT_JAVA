package ru.nsu.fit.oop.lab2.factory;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.lab2.factory.commands.Add;
import ru.nsu.fit.oop.lab2.factory.commands.Define;
import ru.nsu.fit.oop.lab2.factory.commands.Print;
import ru.nsu.fit.oop.lab2.factory.commands.Push;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void execute() {
        List<Pair<Command, List<String>>> commandObjects = new ArrayList<>();
        commandObjects.add(new Pair<>(new Define(), new ArrayList<>(Arrays.asList("a", "2"))));
        commandObjects.add(new Pair<>(new Push(), new ArrayList<>(Collections.singletonList("a"))));
        commandObjects.add(new Pair<>(new Push(), new ArrayList<>(Collections.singletonList("3"))));
        commandObjects.add(new Pair<>(new Add(), new ArrayList<>(Collections.emptyList())));
        commandObjects.add(new Pair<>(new Print(), new ArrayList<>(Collections.emptyList())));

        Interpreter calculator = new Calculator();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

//        calculator.execute(commandObjects);

//        assertEquals(5., new Double(out.toString()));
    }
}