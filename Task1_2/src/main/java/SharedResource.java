import java.util.concurrent.TimeUnit;

class SharedResource implements ISharedResource {
    private long value = 0;
    private final Object lock = new Object();

    public long read() {
        synchronized (lock) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
                return value;
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public void write() {
        synchronized (lock) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
                value++;
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }
}