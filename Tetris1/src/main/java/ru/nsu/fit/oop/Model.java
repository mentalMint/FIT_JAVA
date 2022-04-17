package ru.nsu.fit.oop;

import ru.nsu.fit.oop.shapes.Shape;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static ru.nsu.fit.oop.Color.*;

public class Model extends Observable {
    public class Field {
        public int width = 10;
        public int height = 5;
        public List<Block> blocks = new ArrayList<>();

        public Field() {
            for (int i = 0; i < width * height; ++i) {
                blocks.add(new Block(Color.NO, i % width, i / height));
            }
        }
    }

    private final Field field = new Field();
    private final Timer timer = new Timer();
    private final TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            try {
                nextTick();
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    };

    private Shape currentShape;
    private Shape previousShape;

    private final Set<Class<?>> shapeClasses = new HashSet<>();


    private void nextTick() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        updateCurrentShape();
        updateField();
        notifyObservers();
//        System.out.println(currentShape.y);
    }


    public void start() throws Exception {
        registerShapeClasses();
        createNewShape();
        updateField();
        timer.schedule(timerTask, 0, 1500);
    }

    public void pause() {
        timer.cancel();
    }

    public void resume() {
        timer.schedule(timerTask, 0, 1500);
    }

    private void updateField() {
        for (Block block : previousShape.blocks) {
            field.blocks.get((block.y + previousShape.y) * field.width + block.x + previousShape.x).color = NO;
        }
        for (Block block : currentShape.blocks) {
            field.blocks.get((block.y + currentShape.y) * field.width + block.x + currentShape.x).color = block.color;
        }
//        printField();
    }

    private void updateCurrentShape() throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (landed()) {
            System.err.println("landed");
            createNewShape();
        } else {
            previousShape = new Shape(currentShape);
            currentShape.y++;
        }
    }

    private boolean landed() {
        for (Block block : currentShape.blocks) {
//            System.err.print(block.y + " ");
            if (block.y + currentShape.y == field.height - 1) {
                return true;
            }
        }
//        System.err.println();

        return false;
    }

    public Field getField() {
        return field;
    }

    private void createNewShape() throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?> shapeClass = getRandomElement(this.shapeClasses);
        assert shapeClass != null;
        currentShape = (Shape) shapeClass.getConstructor(Color.class, int.class, int.class).newInstance(RED, 0, 0);
        previousShape = new Shape(currentShape);
//        System.out.println(currentShape);
    }


    private Class<?> getRandomElement(Set<Class<?>> set) {
        int size = set.size();
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for (Class<?> element : set) {
            if (i == item)
                return element;
            i++;
        }
        return null;
    }

    private List<String> parseConfig(InputStream config) {
        List<String> shapeClasses = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(config));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
//                if (words.length != 2) {
//                    System.err.println("\"COMMAND CommandClassName\" was expected");
//                }
                shapeClasses.add(line);
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
        return shapeClasses;
    }

    private void registerShapeClasses() throws Exception {
        InputStream config = Model.class.getResourceAsStream("config.txt");

        if (config == null) {
            throw new NullPointerException("config.txt is not found");
        }
        List<String> shapeClassNames = parseConfig(config);

        for (String shapeClassName : shapeClassNames) {
            try {
                Class<?> shapeClass = Class.forName(shapeClassName);
                this.shapeClasses.add(shapeClass);
            } catch (Exception e) {
                System.err.println("Error while searching shape class: " + e.getLocalizedMessage() + " This shape will be skipped.");
            }
        }
        if (this.shapeClasses.isEmpty()) {
            throw new Exception();
        }
    }

    public void printField() {
        for (int i = 0; i < field.height; ++i) {
            for (int j = 0; j < field.width; ++j) {
                System.out.print(field.blocks.get(i * field.width + j).color + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
