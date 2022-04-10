package ru.nsu.fit.oop.lab2;

import javafx.util.Pair;
import ru.nsu.fit.oop.lab2.factory.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Main {
    private final static Logger logger = Logger.getLogger(Calculator.class.getName());

    public static void main(String[] args) {
        BufferedReader reader = null;
        if (args.length > 0) {
            try {
                reader = new BufferedReader(new FileReader(args[0]));
            } catch (IOException e) {
                System.err.println("Error while reading file: " + e.getLocalizedMessage());
            }
        } else {
            reader = new BufferedReader(new InputStreamReader(System.in));
        }

        if (reader != null) {
            List<String> program = new ArrayList<>();
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    program.add(line);
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

            ProgramParser programParser = new ProgramParser();
            List<Pair<String, List<String>>> commands = programParser.parseProgram(program);

            InputStream config = Calculator.class.getResourceAsStream("config.txt");
            if (config == null) {
                logger.warning("config.txt is not found");
                System.err.println("config.txt is not found");
                return;
            }

            ConfigParser configParser = new ConfigParser();

            Map<String, String> commandClasses;
            commandClasses = configParser.parseConfig(config);

            CommandObjectsCreator commandObjectsCreator = new CommandObjectsCreator();
            List<Pair<Command, List<String>>> commandObjects = commandObjectsCreator.create(commands, commandClasses);

            Calculator calculator = new Calculator();
            calculator.execute(commandObjects);
        }
    }
}
