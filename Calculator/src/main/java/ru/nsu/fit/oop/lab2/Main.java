package ru.nsu.fit.oop.lab2;

import javafx.util.Pair;
import ru.nsu.fit.oop.lab2.factory.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Main {
    public final static Logger logger = Logger.getLogger(Calculator.class.getName());

    public static void main(String[] args) {
        BufferedReader reader = null;
        if (args.length > 0) {
            try {
                reader = new BufferedReader(new FileReader(args[0]));
            } catch (IOException e) {
                logger.severe("Error while reading file.");
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
                logger.severe("Error while reading file.");

            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.severe("Error while closing file.");
                }
            }

            ProgramParser programParser = new ProgramParser();
            List<Pair<String, List<String>>> commands = programParser.parseProgram(program);

            InputStream config = Calculator.class.getResourceAsStream("config.txt");
            if (config == null) {
                logger.severe("config.txt is not found.");
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
