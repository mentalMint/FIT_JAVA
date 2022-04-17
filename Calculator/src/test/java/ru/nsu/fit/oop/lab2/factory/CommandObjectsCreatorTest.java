package ru.nsu.fit.oop.lab2.factory;

import javafx.util.Pair;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import ru.nsu.fit.oop.lab2.factory.commands.Add;
import ru.nsu.fit.oop.lab2.factory.commands.Define;
import ru.nsu.fit.oop.lab2.factory.commands.Print;
import ru.nsu.fit.oop.lab2.factory.commands.Push;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CommandObjectsCreatorTest {

    @Test
    void create() {
        List<Pair<String, List<String>>> commands = new ArrayList<>();
        commands.add(new Pair<>("DEFINE", new ArrayList(Arrays.asList("a", "2"))));
        commands.add(new Pair<>("PUSH", new ArrayList(Collections.singletonList("a"))));
        commands.add(new Pair<>("PUSH", new ArrayList(Collections.singletonList("3"))));
        commands.add(new Pair<>("+", null));
        commands.add(new Pair<>("PRINT", null));
        Properties commandClasses = new Properties();
        commandClasses.put("PUSH", "ru.nsu.fit.oop.lab2.factory.commands.Push");
        commandClasses.put("POP", "ru.nsu.fit.oop.lab2.factory.commands.Pop");
        commandClasses.put("DEFINE", "ru.nsu.fit.oop.lab2.factory.commands.Define");
        commandClasses.put("PRINT", "ru.nsu.fit.oop.lab2.factory.commands.Print");
        commandClasses.put("SQRT", "ru.nsu.fit.oop.lab2.factory.commands.Sqrt");
        commandClasses.put("+", "ru.nsu.fit.oop.lab2.factory.commands.Add");
        commandClasses.put("-", "ru.nsu.fit.oop.lab2.factory.commands.Sub");
        commandClasses.put("*", "ru.nsu.fit.oop.lab2.factory.commands.Multiply");
        commandClasses.put("/", "ru.nsu.fit.oop.lab2.factory.commands.Divide");
        CommandObjectsCreator commandObjectsCreator = new CommandObjectsCreator();

        List<Pair<Command, List<String>>> commandObjects = commandObjectsCreator.create(commands, commandClasses);

        List<Pair<Command, List<String>>> expectedCommandObjects = new ArrayList<>();
        expectedCommandObjects.add(new Pair<>(new Define(), new ArrayList<>(Arrays.asList("a", "2"))));
        expectedCommandObjects.add(new Pair<>(new Push(), new ArrayList<>(Collections.singletonList("a"))));
        expectedCommandObjects.add(new Pair<>(new Push(), new ArrayList<>(Collections.singletonList("3"))));
        expectedCommandObjects.add(new Pair<>(new Add(), null));
        expectedCommandObjects.add(new Pair<>(new Print(), null));
        assertEquals(expectedCommandObjects.size(), commandObjects.size());
    }
}