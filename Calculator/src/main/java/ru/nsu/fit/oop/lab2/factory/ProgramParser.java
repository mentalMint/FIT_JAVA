package ru.nsu.fit.oop.lab2.factory;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class ProgramParser {
    public List<Pair<String, List<String>>> parse(List<String> program) {
        List<Pair<String, List<String>>> commands = new ArrayList<>();
        for (String line : program) {
            if (!line.isEmpty() && line.charAt(0) != '#') {
                List<String> words = new Vector<>(Arrays.asList(line.split(" ")));
                List<String> arguments = null;
                if (words.size() > 1) {
                    arguments = words.subList(1, words.size());
                }
                commands.add(new Pair<>(words.get(0), arguments));
            }
        }
        return commands;
    }
}
