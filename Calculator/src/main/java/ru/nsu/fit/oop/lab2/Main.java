package ru.nsu.fit.oop.lab2;

import javafx.util.Pair;
import ru.nsu.fit.oop.lab2.factory.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public final static Logger logger = Logger.getLogger(Calculator.class.getName());

    public static void main(String[] args) {
        BufferedReader reader = null;
        if (args.length > 0) {
            try {
                reader = new BufferedReader(new FileReader(args[0]));
            } catch (IOException e) {
                logger.log(Level.SEVERE,"Error while reading file.", e);
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
                logger.log(Level.SEVERE,"Error while reading file.", e);
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.log(Level.SEVERE,"Error while closing file.", e);
                }
            }

            ProgramParser programParser = new ProgramParser();
            List<Pair<String, List<String>>> commands = programParser.parse(program);
            InputStream config = Calculator.class.getResourceAsStream("config.txt");
            Calculator calculator = new Calculator();
            try {
                calculator.setProperties(config);
            } catch (Exception e) {
                logger.log(Level.SEVERE,"config.txt is not found.", e);
            }
            calculator.execute(commands);
        }
    }
}
