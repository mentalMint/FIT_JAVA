package ru.nsu.fit.oop.lab2.factory;

import javafx.util.Pair;

import java.util.List;

public interface Interpreter {

    void execute(List<Pair<Command, List<String>>> commandObjects);
}
