package ru.nsu.fit.oop.lab2;

import javafx.util.Pair;
import ru.nsu.fit.oop.lab2.factory.*;
import ru.nsu.fit.oop.lab2.logging.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public final static Logger logger = (Logger) Logger.getLogger(Calculator.class.getName());

    public static void main(String[] args) {
        BufferedReader reader = null;
        if (args.length > 0) {
            try {
                reader = new BufferedReader(new FileReader(args[0]));
            } catch (IOException e) {
                logger.severe("Error while reading file: " + e.getLocalizedMessage());
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
                logger.severe("Error while reading file: " + e.getLocalizedMessage());
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.severe("Error while closing file: " + e.getLocalizedMessage());
                }
            }

            ProgramParser programParser = new ProgramParser();
            List<Pair<String, List<String>>> commands = programParser.parse(program);
            InputStream config = Calculator.class.getResourceAsStream("config.txt");
            Calculator calculator = new Calculator();
            try {
                calculator.setProperties(config);
            } catch (Exception e) {
                logger.severe("config.txt is not found");
            }
            calculator.execute(commands);
        }
    }
}
