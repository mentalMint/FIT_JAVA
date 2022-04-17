package ru.nsu.fit.oop.lab2.factory;

import javafx.util.Pair;

import java.util.*;
import java.util.logging.Logger;

public class CommandObjectsCreator {
    private final static Logger logger = Logger.getLogger(Calculator.class.getName());

    public List<Pair<Command, List<String>>> create(List<Pair<String, List<String>>> commands, Properties commandClasses) {
        List<Pair<Command, List<String>>> commandObjects = new ArrayList<>();
        for (Pair<String, List<String>> command : commands) {
            try {
                Command commandObject = (Command) Class.forName(commandClasses.getProperty(command.getKey())).getConstructor().newInstance();
                commandObjects.add(new Pair<>(commandObject, command.getValue()));
            } catch (Exception e) {
                logger.warning("Error while instantiation. Command " + commandClasses.getProperty(command.getKey()) + " will be skipped.");
            }
        }
        return commandObjects;
    }
}
