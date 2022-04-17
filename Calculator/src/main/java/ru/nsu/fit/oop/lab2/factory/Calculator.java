package ru.nsu.fit.oop.lab2.factory;

import javafx.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.logging.Logger;

public class Calculator implements Interpreter {
    public final static Logger logger = Logger.getLogger(Calculator.class.getName());
    Properties properties = new Properties();

    public void setProperties(InputStream inputStream) throws IOException {
        this.properties.load(inputStream);
    }

    @Override
    public void execute(List<Pair<String, List<String>>> commands) {
        CommandObjectsCreator commandObjectsCreator = new CommandObjectsCreator();
        List<Pair<Command, List<String>>> commandObjects = commandObjectsCreator.create(commands, properties);

        ExecutionContext context = new ExecutionContext();
        for (Pair<Command, List<String>> commandObject : commandObjects) {
            try {
                commandObject.getKey().execute(commandObject.getValue(), context);
            } catch (Exception e) {
                logger.warning("Error while executing command. Command will be skipped.");
            }
        }
    }

    public class ExecutionContext {
        public Stack<Double> stack = new Stack<>();
        public Map<String, Double> parameters = new HashMap<>();
    }
}
