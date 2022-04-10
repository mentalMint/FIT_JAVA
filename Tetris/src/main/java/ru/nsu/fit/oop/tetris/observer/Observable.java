package ru.nsu.fit.oop.tetris.observer;

import java.util.Vector;

public class Observable {
    private Vector<Observer> observers = new Vector<>();

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.handleEvent();
        }
    }
}
