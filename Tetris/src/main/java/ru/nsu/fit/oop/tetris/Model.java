package ru.nsu.fit.oop.tetris;

import ru.nsu.fit.oop.tetris.shapes.Shape;

import javafx.scene.paint.Color;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class Model extends ru.nsu.fit.oop.tetris.observer.Observable {
    public Model() {
        try {
            highScores.load(highScoreFileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum GameState {
        MENU,
        GAME,
        PAUSE,
        SCORE
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

    private class TickTask extends TimerTask {

        @Override
        public void run() {
            try {
                nextTick();
            } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private String highScoreFileName = "high_scores.txt";
    private int score = 0;
    private final HighScores highScores = new HighScores();
    private GameState gameState = GameState.MENU;
    private Shape currentShape;
    private Shape previousShape;
    private final Set<Class<?>> shapeClasses = new HashSet<>();
    private final Set<Color> shapeColors = new HashSet<>(Arrays.asList(Color.DODGERBLUE, Color.SEAGREEN, Color.CRIMSON, Color.GOLD));
    private final Field field = new Field();
    private Timer timer = null;
    private TimerTask timerTask;
    private final int timerSpeed = 700;

    private void nextTick() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        moveCurrentShapeDown();
        if (noWayDown(currentShape)) {
            cleanFullLines();
            if (isOver()) {
                complete();
            }
            createNewShape();
        }
    }

    private boolean isOver() {
        for (int i = 0; i < field.width; i++) {
            if (field.blocks.get(2 * field.width + i).color != Color.TRANSPARENT) {
                return true;
            }
        }
        return false;
    }

    private void cleanFullLines() {
        for (int i = 0; i < field.height; i++) {
            boolean fullLine = true;
            for (int j = 0; j < field.width; j++) {
                if (field.blocks.get(i * field.width + j).color == Color.TRANSPARENT) {
                    fullLine = false;
                    break;
                }
            }
            if (fullLine) {
                score += 10;
                moveBlocksDown(i);
            }
        }
    }

    public void moveBlocksDown(int line) {
        for (int j = 0; j < field.width; j++) {
            field.blocks.get(line * field.width + j).color = Color.TRANSPARENT;
        }
        for (int j = line; j > 0; j--) {
            for (int k = 0; k < field.width; k++) {
                field.blocks.get(j * field.width + k).color = field.blocks.get((j - 1) * field.width + k).color;
            }
        }
        for (int j = 0; j < field.width; j++) {
            field.blocks.get(j).color = Color.TRANSPARENT;
        }
    }

    public void start() throws Exception {
        score = 0;
        cleanBoard();
        gameState = GameState.GAME;
        registerShapeClasses();
        createNewShape();
        updateField();
        notifyObservers();
        timer = new Timer();
        timerTask = new TickTask();
        timer.schedule(timerTask, 1000, timerSpeed);
    }

    public void pause() {
        gameState = GameState.PAUSE;
        timer.cancel();
        notifyObservers();
    }

    public int getScore() {
        return score;
    }

    public void complete() {
        gameState = GameState.SCORE;
        timer.cancel();
        notifyObservers();
    }

    void cleanBoard() {
        for (Block block : field.blocks) {
            block.color = Color.TRANSPARENT;
        }
    }

    public void toMenu() {
        gameState = GameState.MENU;
        notifyObservers();
    }

    public void exit() {
        if (gameState == GameState.GAME) {
            pause();
            toMenu();
        } else if (gameState == GameState.PAUSE) {
            toMenu();
        }
        try {
            highScores.store(highScoreFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        gameState = GameState.GAME;
        timer = new Timer();
        timerTask = new TickTask();
        timer.schedule(timerTask, 0, timerSpeed);
    }

    public void increaseSpeed() {
        changeSpeed(1000);
    }

    public void decreaseSpeed() {
        changeSpeed(timerSpeed);
    }

    private void changeSpeed(int newSpeed) {
        timer.cancel();
        timer = new Timer();
        timerTask = new TickTask();
        timer.schedule(timerTask, 0, newSpeed);
    }

    public Map<String, Integer> getHighScores() {
        return highScores.getHighScores();
    }

    public void addScore(String name) {
        highScores.add(name, score);
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
                currentShape = (Shape) shapeClass.getConstructor(javafx.scene.paint.Color.class, int.class, int.class).newInstance(getRandomElement(shapeColors), field.width / 2 - 1, 1);
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

    private boolean hasIllegalPlace(Block block) {
        if (block.x + currentShape.x < 0 || block.x + currentShape.x >= field.width ||
                block.y + currentShape.y < 0 || block.y + currentShape.y >= field.height) {
            return true;
        }
        Block existingBlock = field.blocks.get((block.y + currentShape.y) * field.width + block.x + currentShape.x);
        return existingBlock.color != Color.TRANSPARENT && !currentShape.blocks.contains(existingBlock);
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
