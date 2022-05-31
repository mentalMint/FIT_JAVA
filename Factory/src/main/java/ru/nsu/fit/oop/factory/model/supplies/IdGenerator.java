package ru.nsu.fit.oop.factory.model.supplies;

import ru.nsu.fit.oop.factory.observer.Observable;

public class IdGenerator extends Observable {
    private int id = 0;

    public synchronized int getNewId() {
        notifyObservers();
        return ++id;
    }

    public synchronized int getCurrentId() {
        return id;
    }
}
