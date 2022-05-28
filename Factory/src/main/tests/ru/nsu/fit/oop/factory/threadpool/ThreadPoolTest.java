package ru.nsu.fit.oop.factory.threadpool;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreadPoolTest {
    @Test
    void threadPoolWithOneThreadTaskExecutionTest() throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(1);
        final Integer[] taskIsCompleted = {0};
        Runnable task = () -> {
            try {
                Thread.sleep(100);
                synchronized (taskIsCompleted) {
                    taskIsCompleted[0]++;
                    if (taskIsCompleted[0] == 5) {
                        taskIsCompleted.notify();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < 10; i++) {
            threadPool.execute(task);
        }

        synchronized (taskIsCompleted) {
            if (taskIsCompleted[0] < 5) {
                taskIsCompleted.wait();
            }
            assertEquals(5, taskIsCompleted[0]);
        }

    }

    @Test
    void threadPoolWithTwoThreadsTaskExecutionTest() throws InterruptedException {
        ThreadPool threadPool = new ThreadPool(3);
        final Integer[] taskIsCompleted = {0};
        Runnable task = () -> {
            try {
                Thread.sleep(100);
                synchronized (taskIsCompleted) {
                    taskIsCompleted[0]++;
                    if (taskIsCompleted[0] == 10) {
                        taskIsCompleted.notify();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        for (int i = 0; i < 10; i++) {
            threadPool.execute(task);
        }

        synchronized (taskIsCompleted) {
            if (taskIsCompleted[0] < 5) {
                taskIsCompleted.wait();
            }
            assertEquals(10, taskIsCompleted[0]);
        }

    }
}