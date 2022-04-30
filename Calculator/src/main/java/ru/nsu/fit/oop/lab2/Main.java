package ru.nsu.fit.oop.lab2;

import javafx.util.Pair;
import ru.nsu.fit.oop.lab2.factory.*;

import java.io.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public final static Logger logger = Logger.getLogger(Calculator.class.getName());

    private static Reader initInputReader(String[] args) {
        Reader reader = null;
        if (args.length > 0) {
            try {
                reader = new FileReader(args[0]);
            } catch (IOException e) {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.severe("Error while reading file: " + e.getLocalizedMessage());
                }
            }
        } else {
            reader = new InputStreamReader(System.in);
        }
        return reader;
    }

    private static List<Pair<String, List<String>>> getCommands(Reader reader) {

        ReaderParser parser = new ReaderParser();
        try {
            parser.parse(reader);
        } catch (IOException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.severe("Error while parsing reader: " + e.getLocalizedMessage());
            }
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                if (logger.isLoggable(Level.SEVERE)) {
                    logger.severe("Error while closing file: " + e.getLocalizedMessage());
                }
            }
        }

        List<String> program = parser.getText();
        ProgramParser programParser = new ProgramParser();
        return programParser.parse(program);
    }

    public static void main(String[] args) {
        Reader reader = initInputReader(args);
        if (reader == null) {
            return;
        }

        List<Pair<String, List<String>>> commands = getCommands(reader);
        Calculator calculator = new Calculator();
        try {
            calculator.setProperties("config.txt");
        } catch (IOException e) {
            if (logger.isLoggable(Level.SEVERE)) {
                logger.severe("config.txt is not found");
            }
        }
        calculator.execute(commands);
    }
}
