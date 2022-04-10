package ru.nsu.fit.oop.lab2.factory;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class CommandObjectsCreator {
    private final static Logger logger = Logger.getLogger(Calculator.class.getName());

    public List<Pair<Command, List<String>>> create(List<Pair<String, List<String>>> commands, Map<String, String> commandClasses) {
        List<Pair<Command, List<String>>> commandObjects = new ArrayList<>();

        for (Pair<String, List<String>> command : commands) {
            try {
                Command commandObject = (Command) Class.forName(commandClasses.get(command.getKey())).newInstance();
                commandObjects.add(new Pair<>(commandObject, command.getValue()));
            } catch (Exception e) {
                logger.warning("Error while instantiation: " + e.getLocalizedMessage() + " Command will be skipped.");
                System.err.println("Error while instantiation: " + e.getLocalizedMessage() + " Command will be skipped.");
            }
        }
        return commandObjects;
    }
}
