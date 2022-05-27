package ru.nsu.fit.oop.factory.threadpool;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ThreadPool {
    private final Queue<Runnable> tasks = new LinkedList<>();
    private final ArrayList<Thread> workers;

    public ThreadPool(int numberOfWorkers) {
        if (numberOfWorkers <= 0) {
            throw new IllegalArgumentException("numberOfWorkers is not positive");
        }

        workers = new ArrayList<>(numberOfWorkers);
        for (int i = 0; i < numberOfWorkers; i++) {
            Worker worker = new Worker(tasks);
            workers.add(worker);
            worker.start();
        }
    }

    public void execute(Runnable task) {
        synchronized (tasks) {
            tasks.add(task);
            tasks.notify();
        }
    }
}
