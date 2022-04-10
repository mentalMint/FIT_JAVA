package ru.nsu.fit.oop.lab2.factory;

import javafx.util.Pair;

import java.util.*;
import java.util.logging.Logger;

public class Calculator implements Interpreter {
    private final static Logger logger = Logger.getLogger(Calculator.class.getName());

    @Override
    public void execute(List<Pair<Command, List<String>>> commandObjects) {
        ExecutionContext context = new ExecutionContext();
        for (Pair<Command, List<String>> commandObject : commandObjects) {
            try {
                commandObject.getKey().execute(commandObject.getValue(), context);
            } catch (Exception e) {
                logger.warning("Error while executing command: " + e.getLocalizedMessage() + " Command will be skipped.");
                System.err.println("Error while executing command: " + e.getLocalizedMessage() + " Command will be skipped.");
            }
        }
    }

    public class ExecutionContext {
        public Stack<Double> stack = new Stack<>();
        public Map<String, Double> parameters = new HashMap<>();
    }
}
