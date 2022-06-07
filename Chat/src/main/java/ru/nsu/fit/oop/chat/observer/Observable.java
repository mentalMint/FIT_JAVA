package ru.nsu.fit.oop.chat.observer;

import ru.nsu.fit.oop.chat.packets.Response;

import java.util.Vector;
import java.util.concurrent.Flow;

public class Observable implements Flow.Publisher<Response> {
    private final Vector<Flow.Subscriber<? super Response>> observers = new Vector<>();

    public void addObserver(Flow.Subscriber<? super Response> subscriber) {
        observers.add(subscriber);
    }

    public void removeObserver(Flow.Subscriber<? super Boolean> observer) {
        observers.remove(observer);
    }

    public void notifyObservers(Response response) {
        for (Flow.Subscriber<? super Response> observer : observers) {
            observer.onNext(response);
        }
    }

//    @Override
//    public synchronized void subscribe(Flow.Subscriber<? super Boolean> subscriber) {
//        addObserver(subscriber);
//    }

    @Override
    public void subscribe(Flow.Subscriber<? super Response> subscriber) {
        addObserver(subscriber);
    }
}
