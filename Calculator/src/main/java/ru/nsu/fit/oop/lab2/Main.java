package ru.nsu.fit.oop.lab2;

import javafx.util.Pair;
import ru.nsu.fit.oop.lab2.factory.*;
import ru.nsu.fit.oop.lab2.logging.Logger;

import java.io.*;
import java.util.List;

public class Main {
    public final static Logger logger = (Logger) Logger.getLogger(Calculator.class.getName());

    public static void main(String[] args) {
        Reader reader = null;
        if (args.length > 0) {
            try {
                reader = new FileReader(args[0]);
            } catch (IOException e) {
                logger.severe("Error while reading file: " + e.getLocalizedMessage());
            }
        } else {
            reader = new InputStreamReader(System.in);
        }

        if (reader != null) {
            ReaderParser parser = new ReaderParser();
            try {
                 parser.parse(reader);
            } catch (IOException e) {
                logger.severe("Error while reading file: " + e.getLocalizedMessage());
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.severe("Error while closing file: " + e.getLocalizedMessage());
                }
            }

            List<String> program = parser.getText();
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
