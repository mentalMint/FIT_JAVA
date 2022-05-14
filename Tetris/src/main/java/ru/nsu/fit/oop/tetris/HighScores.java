package ru.nsu.fit.oop.tetris;

import java.io.*;
import java.util.*;

public class HighScores {
    private final Map<String, Integer> highScores = new TreeMap<>();
    private Properties properties = new Properties();

    public void add(String name, int score) {
        if (highScores.containsKey(name)) {
            if (highScores.get(name) < score) {
                highScores.put(name, score);
            }
        } else {
            highScores.put(name, score);
        }
    }

    public void store(String fileName) throws IOException {
        for (String name : highScores.keySet()) {
            properties.put(name, highScores.get(name).toString());
        }
        PrintWriter writer =
                new PrintWriter(
                        Model.class.getResource(fileName).getPath());
            properties.store(writer, null);
    }

    public void load(String fileName) throws IOException {
        InputStream stream = Model.class.getResourceAsStream(fileName);
        properties.load(stream);
        for (final String name : properties.stringPropertyNames()) {
            highScores.put(name, Integer.parseInt(properties.getProperty(name)));
        }
    }

    public Map<String, Integer> getHighScores() {
        return MapUtil.sortByValue(highScores);
    }
}
