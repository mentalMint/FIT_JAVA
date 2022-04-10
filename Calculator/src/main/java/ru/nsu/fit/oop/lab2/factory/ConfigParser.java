package ru.nsu.fit.oop.lab2.factory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ConfigParser {

    public Map<String, String> parseConfig(InputStream config) {
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
}
