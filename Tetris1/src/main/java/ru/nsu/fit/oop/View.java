package ru.nsu.fit.oop;

import java.util.Observable;
import java.util.Observer;

public class View implements Observer {
    public Model model;

    public View(Model model) {
        this.model = model;
        model.addObserver( this);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
