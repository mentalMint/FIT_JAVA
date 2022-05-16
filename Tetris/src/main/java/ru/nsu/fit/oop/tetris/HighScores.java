package ru.nsu.fit.oop.tetris;

import java.io.*;
import java.net.URL;
import java.util.*;

public class HighScores {
    private final Map<String, Integer> highScores = new TreeMap<>();
    private final Properties properties = new Properties();

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

        URL file = Model.class.getResource(fileName);
        if (file == null) {
            System.err.println("File \"" + fileName + "\" not found. High scores can't be written.");
        } else {
            try (PrintWriter writer = new PrintWriter(Objects.requireNonNull(file).getPath())) {
                properties.store(writer, null);
            } catch (FileNotFoundException e) {
                throw new FileNotFoundException("File \"" + fileName + "\" not found");
            } catch (IOException e) {
                throw new IOException("Error while storing");
            }
        }
    }

    public void load(String fileName) throws IOException {
        try (InputStream stream = Model.class.getResourceAsStream(fileName)) {
            if (stream == null) {
                System.err.println("File \"" + fileName + "\" not found. High scores can't be loaded.");
            } else {
                properties.load(stream);
            }
        } catch (IOException e) {
            throw new IOException("Error while loading");
        }
        for (final String name : properties.stringPropertyNames()) {
            highScores.put(name, Integer.parseInt(properties.getProperty(name)));
        }
    }

    public Map<String, Integer> getHighScores() {
        return MapUtil.sortByValue(highScores);
    }
}
