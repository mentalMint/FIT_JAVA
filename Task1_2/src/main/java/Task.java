import java.util.concurrent.TimeUnit;

public class Task implements Runnable {
    private String name;

    public Task(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void run() {
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}