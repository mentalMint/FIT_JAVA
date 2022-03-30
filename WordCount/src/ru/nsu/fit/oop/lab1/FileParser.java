package ru.nsu.fit.oop.lab1;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileParser {
    private final ArrayList<String> text = new ArrayList<>();

    public FileParser() {
    }

    public FileParser(String inputFile) {
        readInputFile(inputFile);
    }

    public List<String> getText() {
        return this.text;
    }

    public void readInputFile(String inputFile) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inputFile));
            String line;
            while ((line = reader.readLine()) != null) {
                text.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error while reading file: " + e.getLocalizedMessage());
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }
    }
}
