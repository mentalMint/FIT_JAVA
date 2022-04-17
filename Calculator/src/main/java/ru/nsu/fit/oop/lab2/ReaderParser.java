package ru.nsu.fit.oop.lab2;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReaderParser {
    private final ArrayList<String> text = new ArrayList<>();

    public ReaderParser() {
    }

    public List<String> getText() {
        return this.text;
    }

    public void parse(Reader reader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            text.add(line);
        }
    }
}