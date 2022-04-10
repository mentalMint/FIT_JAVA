package ru.nsu.fit.oop.lab2.factory;

import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProgramParserTest {

    @Test
    void parseProgram() {
        List<String> program = new ArrayList<>();
        program.add("DEFINE b 3");
        program.add("PUSH 15");
        program.add("#comment PUSH 1");
        program.add("PUSH b");
        program.add("PRINT");

        ProgramParser programParser = new ProgramParser();
        List<Pair<String, List<String>>> commands = programParser.parseProgram(program);

        List<Pair<String, List<String>>> expectedCommands = new ArrayList<>();
        expectedCommands.add(new Pair<>("DEFINE", new ArrayList(Arrays.asList("b", "3"))));
        expectedCommands.add(new Pair<>("PUSH", new ArrayList(Collections.singletonList("15"))));
        expectedCommands.add(new Pair<>("PUSH", new ArrayList(Collections.singletonList("b"))));
        expectedCommands.add(new Pair<>("PRINT", null));

        assertEquals(expectedCommands, commands);
    }
}