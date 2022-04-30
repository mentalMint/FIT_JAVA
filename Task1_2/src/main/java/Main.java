import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static class ReadThread extends Thread {
        ISharedResource sharedResource;
        FileWriter writer;
        int success = 0;
        int failure = 0;

        public ReadThread(ISharedResource sharedResource, FileWriter writer) {
            this.sharedResource = sharedResource;
            this.writer = writer;
        }

        @Override
        public void interrupt() {
            System.out.println("Read success: " + success + " failure: " + failure);
            try {
                writer.write("Read success: " + success + " failure: " + failure + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.interrupt();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    sharedResource.read();
                    success++;
                } catch (IllegalStateException e) {
                    failure++;
                }
            }
        }
    }

    public static class WriteThread extends Thread {
        ISharedResource sharedResource;
        FileWriter writer;
        int success = 0;
        int failure = 0;

        public WriteThread(ISharedResource sharedResource, FileWriter writer) {
            this.sharedResource = sharedResource;
            this.writer = writer;
        }

        @Override
        public void interrupt() {
            System.out.println("Write success: " + success + " failure: " + failure);
            try {
                writer.write("Write success: " + success + " failure: " + failure + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            super.interrupt();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    sharedResource.write();
                    success++;
                } catch (IllegalStateException | InterruptedException e) {
                    failure++;
                }
            }
        }
    }

    public static void main(String[] args) {
        FileWriter writer = null;
        try {
            writer = new FileWriter("statistics.txt", true);
//            writer.write("synchronized\n");
            writer.write("reentrant\n");
        } catch (IOException e) {
            System.err.println("Error while reading file: " + e.getLocalizedMessage());
            try {
                assert writer != null;
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }

//        ISharedResource sharedResource = new SharedResource();
        ISharedResource sharedResource = new SharedResource2();

        ArrayList<ReadThread> readThreads = new ArrayList<>();
        ArrayList<WriteThread> writeThreads = new ArrayList<>();

        long start = System.currentTimeMillis();
        long end = start + 60 * 1000;

        for (int i = 0; i < 15; ++i) {
            readThreads.add(new ReadThread(sharedResource, writer));
        }
        for (int i = 0; i < 5; ++i) {
            writeThreads.add(new WriteThread(sharedResource, writer));
        }
        for (Thread thread : readThreads) {
            thread.start();
        }
        for (Thread thread : writeThreads) {
            thread.start();
        }

        while (System.currentTimeMillis() < end) {

        }

        for (Thread thread : readThreads) {
            thread.interrupt();
        }
        for (Thread thread : writeThreads) {
            thread.interrupt();
        }

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}
