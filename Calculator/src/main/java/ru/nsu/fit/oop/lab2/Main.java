package ru.nsu.fit.oop.lab2;

import ru.nsu.fit.oop.lab2.factory.Calculator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        BufferedReader reader = null;
        if (args.length > 0) {
            try {
                reader = new BufferedReader(new FileReader(args[0]));
            } catch (IOException e) {
                System.err.println("Error while reading file: " + e.getLocalizedMessage());
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
                System.err.println("Error while reading file: " + e.getLocalizedMessage());
            } finally {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }

            Calculator calculator = new Calculator();
            calculator.execute(program);
        }
    }
}
