import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class SharedResource2 implements ISharedResource{
    private long value = 0;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock lock2 = new ReentrantReadWriteLock();

    public long read() {
        lock.readLock().lock();
        try {
            TimeUnit.MILLISECONDS.sleep(100);
            return value;
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void write() throws InterruptedException {
        lock2.writeLock().lockInterruptibly();
        try {
            TimeUnit.MILLISECONDS.sleep(100);
            value++;
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } finally {
            lock2.writeLock().unlock();
        }
    }
}