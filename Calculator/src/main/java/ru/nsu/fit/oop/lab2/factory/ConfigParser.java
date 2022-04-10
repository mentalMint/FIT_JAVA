package ru.nsu.fit.oop.lab2.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class ConfigParser {
    public final static Logger logger = Logger.getLogger(Calculator.class.getName());


    public Map<String, String> parseConfig(InputStream config) {
        HashMap<String, String> commandClasses = new HashMap<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(config));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(" ");
                if (words.length != 2) {
                    logger.warning("\"COMMAND CommandClassName\" was expected.");
                }
                commandClasses.put(words[0], words[1]);
            }
        } catch (IOException e) {
            logger.severe("Error while reading file.");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
        return commandClasses;
    }
}
