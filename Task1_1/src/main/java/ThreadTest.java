import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThreadTest {
    public static void main(String[] args) {
        int threadsNumber = 100000;
        FileWriter writer = null;
        try {
            writer = new FileWriter("statistics.txt", true);
            writer.write("Threads:\nThreads number: " + threadsNumber + "\n");
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

        for (int j = 0; j < 3; j++) {
            Task task = new Task();
            long start, end, end2;
            start = System.currentTimeMillis();
            List<Thread> threads = new ArrayList<>();
            for (int i = 0; i < threadsNumber; i++) {
                threads.add(new Thread(task));
            }

//            start2 = System.currentTimeMillis();
            for (Thread thread: threads) {
                thread.start();
            }
            end = System.currentTimeMillis();

            for (Thread thread: threads) {
                try {
                    thread.join();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

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
