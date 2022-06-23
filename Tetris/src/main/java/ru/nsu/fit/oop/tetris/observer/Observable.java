package ru.nsu.fit.oop.tetris.observer;

import java.util.Vector;
import java.util.concurrent.Flow;

public class Observable implements Flow.Publisher<Object> {
    private final Vector<Flow.Subscriber<? super Object>> observers = new Vector<>();

    public void addObserver(Flow.Subscriber<? super Object> observer) {
        observers.add(observer);
    }

    public void removeObserver(Flow.Subscriber<? super Object> observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Flow.Subscriber<? super Object> observer : observers) {
            observer.onNext(null);
        }
    }

    @Override
    public synchronized void subscribe(Flow.Subscriber<? super Object> subscriber) {
        addObserver(subscriber);
    }
}
