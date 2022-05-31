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
        try {

            while (!interrupted()) {
                synchronized (tasks) {
                    while (tasks.isEmpty()) {
                        tasks.wait();
                    }
                    task = tasks.remove();
                }
                task.run();
            }
        } catch (InterruptedException e) {
//            e.printStackTrace();
//            return;
        }
        System.err.println(Thread.currentThread().getName() + " has stopped");

    }
}