package ru.nsu.fit.oop.lab2.factory;

import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Logger;

public class Calculator implements Interpreter {
    private final static Logger logger = Logger.getLogger(Calculator.class.getName());

    private List<Pair<String, List<String>>> parseProgram(List<String> program) {
        List<Pair<String, List<String>>> commands = new ArrayList<>();
        for (String line : program) {
            if (line.charAt(0) != '#') {
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

    private Map<String, String> parseConfig(InputStream config) {
        HashMap<String, String> commandClasses = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(config));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(" ");
                if (words.length != 2) {
                    System.err.println("\"COMMAND CommandClassName\" was expected");
                }
                commandClasses.put(words[0], words[1]);
            }
        } catch (IOException e) {
            System.err.println("Error while reading file: " + e.getLocalizedMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
        return commandClasses;
    }

    @Override
    public void execute(List<String> program) {
        List<Pair<String, List<String>>> commands = parseProgram(program);
        List<Pair<Command, List<String>>> commandObjects = new ArrayList<>();
        InputStream config = Calculator.class.getResourceAsStream("config.txt");

        if (config == null) {
            logger.warning("config.txt is not found");
            throw new NullPointerException("config.txt is not found");
        }
        Map<String, String> commandClasses = parseConfig(config);

        for (Pair<String, List<String>> command : commands) {
            try {
                Command commandObject = (Command) Class.forName(commandClasses.get(command.getKey())).newInstance();
                commandObjects.add(new Pair<>(commandObject, command.getValue()));
            } catch (Exception e) {
                logger.warning("Error while instantiation: " + e.getLocalizedMessage() + " Command will be skipped.");
                System.err.println("Error while instantiation: " + e.getLocalizedMessage() + " Command will be skipped.");
            }
        }

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
