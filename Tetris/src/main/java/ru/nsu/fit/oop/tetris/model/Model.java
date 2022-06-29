package ru.nsu.fit.oop.tetris.model;

import javafx.fxml.LoadException;
import ru.nsu.fit.oop.tetris.exceptions.ClassesRegistrationException;
import ru.nsu.fit.oop.tetris.exceptions.ShapeCreationException;
import ru.nsu.fit.oop.tetris.model.shapes.Shape;

import javafx.scene.paint.Color;
import ru.nsu.fit.oop.tetris.utilities.SetRandomizer;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Model extends ru.nsu.fit.oop.tetris.observer.Observable {
    private final String highScoreFileName = "high_scores.txt";
    private int score = 0;
    private final HighScores highScores = new HighScores();
    private GameState gameState = GameState.MENU;
    private Shape currentShape;
    private Shape previousShape;
    private final Set<Class<?>> shapeClasses = new HashSet<>();
    private final Set<Color> shapeColors = new HashSet<>(Arrays.asList(Color.DODGERBLUE,
            Color.MEDIUMAQUAMARINE, Color.PALEVIOLETRED, Color.GOLD));
    private final int fieldWidth = 10;
    private final int fieldHeight = 14;
    private final Field field = new Field(fieldWidth, fieldHeight);
    private final TetrisTimer tetrisTimer = new TetrisTimer();

    public Model() {
        try {
            highScores.load(highScoreFileName);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("High scores will be empty.");
        }
    }

    private void nextTick() throws ShapeCreationException {
        if (field.noWayDown(currentShape)) {
            updateScore(field.cleanFullLines());
            if (isOver()) {
                complete();
            }
            createNewShape();
        }
        moveCurrentShapeDown();
    }

    private void updateScore(int fullLinesCount) {
        if (fullLinesCount == 1) {
            score += 10;
        } else if (fullLinesCount == 2) {
            score += 20 * 1.5;
        } else if (fullLinesCount == 3) {
            score += 30 * 2;
        } else if (fullLinesCount == 4) {
            score += 40 * 2.5;
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

    public void start() throws ClassesRegistrationException, ShapeCreationException {
        score = 0;
        field.clean();
        gameState = GameState.GAME;
        try {
            registerShapeClasses();
        } catch (Exception e) {
            throw new ClassesRegistrationException(e);
        }
        createNewShape();
        updateField();

        notifyObservers();
        tetrisTimer.start();
    }

    public void pause() {
        gameState = GameState.PAUSE;
        tetrisTimer.cancel();
        notifyObservers();
    }

    public int getScore() {
        return score;
    }

    public void complete() {
        gameState = GameState.SCORE;
        tetrisTimer.cancel();
        notifyObservers();
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
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("High scores won't be updated.");
        }
    }

    public void resume() {
        gameState = GameState.GAME;
        tetrisTimer.resume();
    }

    public void increaseSpeed() {
        tetrisTimer.increaseSpeed();
    }

    public void decreaseSpeed() {
        tetrisTimer.decreaseSpeed();
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

    private void updateField() throws ShapeCreationException {
        for (Block block : previousShape.blocks) {
            block.color = Color.TRANSPARENT;
            field.blocks.set((block.y + previousShape.y) * field.width + block.x + previousShape.x, block);
        }
        for (Block block : currentShape.blocks) {
            field.blocks.set((block.y + currentShape.y) * field.width + block.x + currentShape.x, block);
        }
        try {
            previousShape = currentShape.getClass().getConstructor(currentShape.getClass()).newInstance(currentShape);
        } catch (Exception e) {
            throw new ShapeCreationException(e);
        }
    }

    private void moveCurrentShapeDown() throws ShapeCreationException {
        if (!field.noWayDown(currentShape)) {
            currentShape.y++;
            updateField();
            notifyObservers();
        }
    }

    private void createNewShape() throws ShapeCreationException {
        Class<?> shapeClass = SetRandomizer.getRandomElement(this.shapeClasses);
        if (shapeClass == null) {
            throw new NullPointerException("Null shape class");
        } else {
            try {
                currentShape = (Shape) shapeClass.getConstructor(javafx.scene.paint.Color.class, int.class, int.class).
                        newInstance(SetRandomizer.getRandomElement(shapeColors), field.width / 2 - 1, 1);
            } catch (Exception e) {
                throw new ShapeCreationException(e);
            }
        }

        try {
            previousShape = currentShape.getClass().getConstructor(currentShape.getClass()).newInstance(currentShape);
        } catch (Exception e) {
            throw new ShapeCreationException(e);
        }
    }

    private void registerShapeClasses() throws NullPointerException, LoadException, ClassNotFoundException {
        InputStream config = Model.class.getResourceAsStream("config.txt");
        if (config == null) {
            throw new NullPointerException("config.txt is not found");
        }
        Properties properties = new Properties();
        try {
            properties.load(config);
        } catch (IOException e) {
            throw new LoadException("Failed to load config");
        }

        for (Object shapeClassName : properties.values()) {
            try {
                Class<?> shapeClass = Class.forName((String) shapeClassName);
                this.shapeClasses.add(shapeClass);
            } catch (ClassNotFoundException e) {
                System.err.println("Error while searching shape class: " +
                        e.getLocalizedMessage() + " This shape will be skipped");
            }
        }
        shapeClasses.remove(null);

        if (this.shapeClasses.isEmpty()) {
            throw new ClassNotFoundException("No shape classes from config.txt found");
        }
    }

    public void moveCurrentShapeRight() throws ShapeCreationException {
        if (!field.noWayRight(currentShape)) {
            currentShape.x++;
            updateField();
            notifyObservers();
        }
    }

    public void moveCurrentShapeLeft() throws ShapeCreationException {
        if (!field.noWayLeft(currentShape)) {
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

    public void rotateCurrentShape() throws ShapeCreationException {
        Shape tmp;
        try {
            tmp = currentShape.getClass().getConstructor(currentShape.getClass()).newInstance(currentShape);
        } catch (Exception e) {
            throw new ShapeCreationException(e);
        }
        tmp.rotate();
        for (Block block : tmp.blocks) {
            if (hasIllegalPlace(block)) {
                return;
            }
        }
        currentShape = tmp;
        updateField();
        notifyObservers();
    }

    public enum GameState {
        MENU, GAME, PAUSE, SCORE
    }

    public class Field {
        private final int width;
        private final int height;
        private final List<Block> blocks = new ArrayList<>();

        public Field(int width, int height) {
            this.width = width;
            this.height = height;
            for (int i = 0; i < width * height; ++i) {
                blocks.add(new Block(javafx.scene.paint.Color.TRANSPARENT, i % width, i / height));
            }
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public List<Block> getBlocks() {
            return blocks;
        }

        private void clean() {
            for (Block block : this.blocks) {
                block.color = Color.TRANSPARENT;
            }
        }

        private int cleanFullLines() {
            int fullLinesCount = 0;
            for (int i = 0; i < this.height; i++) {
                boolean fullLine = true;
                for (int j = 0; j < this.width; j++) {
                    if (this.blocks.get(i * this.width + j).color == Color.TRANSPARENT) {
                        fullLine = false;
                        break;
                    }
                }
                if (fullLine) {
                    fullLinesCount++;
                    moveBlocksDown(i);
                }
            }
            return fullLinesCount;
        }

        private void moveBlocksDown(int line) {
            for (int j = 0; j < width; j++) {
                blocks.get(line * width + j).color = Color.TRANSPARENT;
            }
            for (int j = line; j > 0; j--) {
                for (int k = 0; k < width; k++) {
                    blocks.get(j * width + k).color = blocks.get((j - 1) * width + k).color;
                }
            }
            for (int j = 0; j < width; j++) {
                blocks.get(j).color = Color.TRANSPARENT;
            }
        }

        public boolean noWayDown(Shape shape) {
            for (Block block : shape.blocks) {
                int blockYCoordinates = block.y + shape.y;
                if (blockYCoordinates >= height - 1) {
                    return true;
                }
                Block lowerBlock = blocks.get(blockYCoordinates * width + block.x + shape.x + width);
                if (lowerBlock.color != Color.TRANSPARENT && !shape.blocks.contains(lowerBlock)) {
                    return true;
                }
            }
            return false;
        }

        public boolean noWayUp(Shape shape) {
            for (Block block : shape.blocks) {
                int blockYCoordinates = block.y + shape.y;
                if (blockYCoordinates <= 0) {
                    return true;
                }
                Block upperBlock = blocks.get(blockYCoordinates * width + block.x + shape.x - width);
                if (upperBlock.color != Color.TRANSPARENT && !shape.blocks.contains(upperBlock)) {
                    return true;
                }
            }
            return false;
        }

        public boolean noWayLeft(Shape shape) {
            for (Block block : shape.blocks) {
                int blockXCoordinates = block.x + shape.x;
                if (blockXCoordinates <= 0) {
                    return true;
                } else {
                    Block lefterBlock = blocks.get(blockXCoordinates - 1 + (block.y + shape.y) * width);
                    if (lefterBlock.color != Color.TRANSPARENT && !shape.blocks.contains(lefterBlock)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean noWayRight(Shape shape) {
            for (Block block : shape.blocks) {
                int blockXCoordinates = block.x + shape.x;
                if (blockXCoordinates >= width - 1) {
                    return true;
                } else {
                    Block lefterBlock = blocks.get(blockXCoordinates + 1 + (block.y + shape.y) * width);
                    if (lefterBlock.color != Color.TRANSPARENT && !shape.blocks.contains(lefterBlock)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    private class TickTask extends TimerTask {
        @Override
        public void run() {
            try {
                nextTick();
            } catch (ShapeCreationException e) {
                pause();
                exit();
                e.printStackTrace();
            }
        }
    }

    private class TetrisTimer {
        private Timer timer = null;
        private TimerTask timerTask;
        private final int timerSpeed = 700;
        private final int delay = 1000;

        public TetrisTimer() {

        }

        public void start() {
            timer = new Timer();
            timerTask = new TickTask();
            timer.schedule(timerTask, delay, timerSpeed);
        }

        public void cancel() {
            if (timer != null) {
                timer.cancel();
            }
        }

        public void resume() {
            timer = new Timer();
            timerTask = new TickTask();
            timer.schedule(timerTask, 0, timerSpeed);
        }

        public void increaseSpeed() {
            changeSpeed(delay);
        }

        public void decreaseSpeed() {
            changeSpeed(timerSpeed);
        }

        private void changeSpeed(int newSpeed) {
            if (timer != null && timerSpeed != newSpeed) {
                timer.cancel();
                timer = new Timer();
                timerTask = new TickTask();
                timer.schedule(timerTask, 0, newSpeed);
            }
        }
    }

}
