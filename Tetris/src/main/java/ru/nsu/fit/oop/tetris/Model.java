package ru.nsu.fit.oop.tetris;

import ru.nsu.fit.oop.tetris.shapes.Shape;

import javafx.scene.paint.Color;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Model extends ru.nsu.fit.oop.tetris.observer.Observable {
    public enum GameState {
        MENU,
        HIGH_SCORES,
        ABOUT,
        GAME,
        OVER,
        PAUSE
    }
    public class Field {
        private final int width = 10;
        private final int height = 17;
        private final List<Block> blocks = new ArrayList<>();

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public List<Block> getBlocks() {
            return blocks;
        }

        public Field() {
            for (int i = 0; i < width * height; ++i) {
                blocks.add(new Block(javafx.scene.paint.Color.TRANSPARENT, i % width, i / height));
            }
        }
    }

    private GameState gameState = GameState.MENU;
    private Shape currentShape;
    private Shape previousShape;
    private final Set<Class<?>> shapeClasses = new HashSet<>();
    private final Set<Color> shapeColors = new HashSet<>(Arrays.asList(Color.DODGERBLUE, Color.SEAGREEN, Color.CRIMSON, Color.GOLD));
    private final Field field = new Field();
    private Timer timer = null;
    private TimerTask timerTask;


    private void nextTick() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        moveCurrentShapeDown();
        if (noWayDown(currentShape)) {
            createNewShape();
        }
    }

    public void start() throws Exception {
        gameState = GameState.GAME;
        registerShapeClasses();
        createNewShape();
        updateField();
        notifyObservers();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    nextTick();
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 1000, 700);
    }

    public void pause() {
        gameState = GameState.PAUSE;
        timer.cancel();
        notifyObservers();
    }

    void cleanBoard() {
        for (Block block : field.blocks) {
            block.color = Color.TRANSPARENT;
        }
    }

    public void stop() {
        gameState = GameState.MENU;
        notifyObservers();
        cleanBoard();
    }

    public void exit() {
        if (gameState == GameState.GAME) {
            pause();
            stop();
        } else if (gameState == GameState.PAUSE) {
            stop();
        }
    }

    public void resume() {
        gameState = GameState.GAME;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    nextTick();
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 0, 700);
    }

    public Field getField() {
        return field;
    }

    public GameState getGameState() {
        return gameState;
    }
    private void updateField() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        for (Block block : previousShape.blocks) {
            block.color = Color.TRANSPARENT;
            field.blocks.set((block.y + previousShape.y) * field.width + block.x + previousShape.x, block);
        }
        for (Block block : currentShape.blocks) {
            field.blocks.set((block.y + currentShape.y) * field.width + block.x + currentShape.x, block);
        }
        previousShape = currentShape.getClass().getConstructor(currentShape.getClass()).newInstance(currentShape);

        if (noWayDown(currentShape)) {
            for (int i = 0; i < field.height; i++) {
                boolean fullLine = true;
                for (int j = 0; j < field.width; j++) {
                    if (field.blocks.get(i * field.width + j).color == Color.TRANSPARENT) {
                        fullLine = false;
                        break;
                    }
                }
                if (fullLine) {
                    for (int j = 0; j < field.width; j++) {
                        field.blocks.get(i * field.width + j).color = Color.TRANSPARENT;
                    }
                    for (int j = i; j > 0; j--) {
                        for (int k = 0; k < field.width; k++) {
                            field.blocks.get(j * field.width + k).color = field.blocks.get((j - 1) * field.width + k).color;
                        }
                    }
                    for (int j = 0; j < field.width; j++) {
                        field.blocks.get(j).color = Color.TRANSPARENT;
                    }
                }
            }
        }
    }

    private void moveCurrentShapeDown() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!noWayDown(currentShape)) {
            currentShape.y++;
            updateField();
            notifyObservers();
        }
    }

    private void createNewShape() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> shapeClass = getRandomElement(this.shapeClasses);
        if (shapeClass == null) {
            System.err.println("Null shape class");
        } else {
            try {
                currentShape = (Shape) shapeClass.getConstructor(javafx.scene.paint.Color.class, int.class, int.class).newInstance(getRandomElement(shapeColors), field.width / 2 - 1, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        previousShape = currentShape.getClass().getConstructor(currentShape.getClass()).newInstance(currentShape);
    }

    private <T> T getRandomElement(Set<T> set) {
        int size = set.size();
        int item = new Random().nextInt(size);
        int i = 0;
        for (T element : set) {
            if (i == item)
                return element;
            i++;
        }
        return null;
    }

    private void registerShapeClasses() throws Exception {
        InputStream config = Model.class.getResourceAsStream("config.txt");
        if (config == null) {
            throw new NullPointerException("config.txt is not found");
        }
        Properties properties = new Properties();
        properties.load(config);

        for (Object shapeClassName : properties.values()) {
            try {
                Class<?> shapeClass = Class.forName((String) shapeClassName);
                this.shapeClasses.add(shapeClass);
            } catch (Exception e) {
                System.err.println("Error while searching shape class: " + e.getLocalizedMessage() + " This shape will be skipped.");
            }
        }

        if (this.shapeClasses.isEmpty()) {
            throw new Exception("no shape classes");
        }
    }

    private boolean noWayDown(Shape shape) {
        for (Block block : shape.blocks) {
            int blockYCoordinates = block.y + shape.y;
            if (blockYCoordinates >= field.height - 1) {
                return true;
            }
            Block lowerBlock = field.blocks.get(blockYCoordinates * field.width + block.x + shape.x + field.width);
            if (lowerBlock.color != Color.TRANSPARENT && !shape.blocks.contains(lowerBlock)) {
                return true;
            }
        }
        return false;
    }

    private boolean noWayUp(Shape shape) {
        for (Block block : shape.blocks) {
            int blockYCoordinates = block.y + shape.y;
            if (blockYCoordinates <= 0) {
                return true;
            }
            Block upperBlock = field.blocks.get(blockYCoordinates * field.width + block.x + shape.x - field.width);
            if (upperBlock.color != Color.TRANSPARENT && !shape.blocks.contains(upperBlock)) {
                return true;
            }
        }
        return false;
    }

    private boolean noWayLeft(Shape shape) {
        for (Block block : shape.blocks) {
            int blockXCoordinates = block.x + shape.x;
            if (blockXCoordinates <= 0) {
                return true;
            } else {
                Block lefterBlock = field.blocks.get(blockXCoordinates - 1 + (block.y + shape.y) * field.width);
                if (lefterBlock.color != Color.TRANSPARENT && !shape.blocks.contains(lefterBlock)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean noWayRight(Shape shape) {
        for (Block block : shape.blocks) {
            int blockXCoordinates = block.x + shape.x;
            if (blockXCoordinates >= field.width - 1) {
                return true;
            } else {
                Block lefterBlock = field.blocks.get(blockXCoordinates + 1 + (block.y + shape.y) * field.width);
                if (lefterBlock.color != Color.TRANSPARENT && !shape.blocks.contains(lefterBlock)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void moveCurrentShapeRight() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!noWayRight(currentShape)) {
            currentShape.x++;
            updateField();
            notifyObservers();
        }
    }

    public void moveCurrentShapeLeft() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        if (!noWayLeft(currentShape)) {
            currentShape.x--;
            updateField();
            notifyObservers();
        }
    }

    boolean isOutOfBoundaries(Shape shape) {
        for (Block block : shape.blocks) {
            if (shape.x + block.x < 0 || shape.x + block.x >= field.width ||
                    shape.y + block.y < 0 || shape.y + block.y >= field.width) {
                return true;
            }
        }
        return false;
    }

    int getIndexOfLeftestBlock(Shape shape) {
        int indexOfLeftest = 0;
        for (Block block : shape.blocks) {
            if (block.x < shape.blocks.get(indexOfLeftest).x) {
                indexOfLeftest = shape.blocks.indexOf(block);
            }
        }
        return indexOfLeftest;
    }

    int getIndexOfRightestBlock(Shape shape) {
        int indexOfRightest = 0;
        for (Block block : shape.blocks) {
            if (block.x > shape.blocks.get(indexOfRightest).x) {
                indexOfRightest = shape.blocks.indexOf(block);
            }
        }
        return indexOfRightest;
    }

    int getIndexOfHighestBlock(Shape shape) {
        int indexOfHighest = 0;
        for (Block block : shape.blocks) {
            if (block.y < shape.blocks.get(indexOfHighest).y) {
                indexOfHighest = shape.blocks.indexOf(block);
            }
        }
        return indexOfHighest;
    }

    int getIndexOfLowestBlock(Shape shape) {
        int indexOfLowest = 0;
        for (Block block : shape.blocks) {
            if (block.y > shape.blocks.get(indexOfLowest).y) {
                indexOfLowest = shape.blocks.indexOf(block);
            }
        }
        return indexOfLowest;
    }

    boolean hasIllegalPlace(Block block) {
        Block existingBlock = field.blocks.get((block.y + currentShape.y) * field.width + block.x + currentShape.x);
        return block.x + currentShape.x < 0 || block.x + currentShape.x >= field.width ||
                block.y + currentShape.y < 0 || block.y + currentShape.y >= field.height ||
                (existingBlock.color != Color.TRANSPARENT &&
                        ! currentShape.blocks.contains(existingBlock));
    }

    public void rotateCurrentShape() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Shape tmp = currentShape.getClass().getConstructor(currentShape.getClass()).newInstance(currentShape);
        currentShape.rotate();
        for (Block block : currentShape.blocks) {
            if (hasIllegalPlace(block)) {
                currentShape = tmp;
                return;
            }
        }
        updateField();
        notifyObservers();
    }
}
