package ru.nsu.fit.oop.lab2.factory;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ConfigParserTest {

    @Test
    void parseConfig() {
        ConfigParser configParser = new ConfigParser();
        InputStream config = Calculator.class.getResourceAsStream("config.txt");
        if (config == null) {
            System.err.println("config.txt is not found");
            return;
        }

        Map<String, String> commandClasses = configParser.parseConfig(config);

        Map<String, String> expectedCommandClasses = new HashMap<>();
        expectedCommandClasses.put("PUSH", "ru.nsu.fit.oop.lab2.factory.commands.Push");
        expectedCommandClasses.put("POP", "ru.nsu.fit.oop.lab2.factory.commands.Pop");
        expectedCommandClasses.put("DEFINE", "ru.nsu.fit.oop.lab2.factory.commands.Define");
        expectedCommandClasses.put("PRINT", "ru.nsu.fit.oop.lab2.factory.commands.Print");
        expectedCommandClasses.put("SQRT", "ru.nsu.fit.oop.lab2.factory.commands.Sqrt");
        expectedCommandClasses.put("+", "ru.nsu.fit.oop.lab2.factory.commands.Add");
        expectedCommandClasses.put("-", "ru.nsu.fit.oop.lab2.factory.commands.Sub");
        expectedCommandClasses.put("*", "ru.nsu.fit.oop.lab2.factory.commands.Multiply");
        expectedCommandClasses.put("/", "ru.nsu.fit.oop.lab2.factory.commands.Divide");
        assertEquals(expectedCommandClasses, commandClasses);
    }
}