package ru.nsu.fit.oop.tetris.View;
import java.util.concurrent.Flow;

public interface IView extends Flow.Subscriber<Boolean> {
    void prepareScene();
}
