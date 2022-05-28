package ru.nsu.fit.oop.factory.threadpool;

import java.util.Queue;

public class Worker extends Thread {
    private final Queue<Runnable> tasks;

    public Worker(Queue<Runnable> tasks) {
        this.tasks = tasks;
    }

    @Override
    public void run() {
        super.run();
        Runnable task;
        while (true) {
            synchronized (tasks) {
                if (tasks.isEmpty()) {
                    try {
                        tasks.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (tasks.isEmpty()) {
                    continue;
                }
                task = tasks.remove();
            }
            task.run();

        }
    }
}