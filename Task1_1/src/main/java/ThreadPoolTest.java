import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.*;

public class ThreadPoolTest {
    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        int threadsNumber = 10;
        FileWriter writer = null;
        try {
            writer = new FileWriter("statistics.txt", true);
            writer.write("ThreadPool:\nThreads number: " + threadsNumber + "\n");
        } catch (IOException e) {
            System.err.println("Error while reading file: " + e.getLocalizedMessage());
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace(System.err);
                }
            }
        }

        for (int j = 0; j < 1; j++) {
            Task task = new Task();
            long start, end, end2;
            start = System.currentTimeMillis();
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadsNumber);
            end = System.currentTimeMillis();

            for (int i = 0; i < 100000; i++) {
                executor.execute(task);
            }

            awaitTerminationAfterShutdown(executor);
            end2 = System.currentTimeMillis();

            System.out.println(task.getCounter());
            System.out.println("Creation time: " + (end - start) + " milliseconds");
            System.out.println("All time: " + (end2 - start) + " milliseconds");
            try {
                writer = new FileWriter("statistics.txt", true);
                writer.write("Creation time: " + (end - start) + " milliseconds\n");
                writer.write("All time: " + (end2 - start) + " milliseconds\n\n");
            } catch (IOException e) {
                System.err.println("Error while reading file: " + e.getLocalizedMessage());
            } finally {
                if (null != writer) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace(System.err);
                    }
                }
            }
        }
    }
}
