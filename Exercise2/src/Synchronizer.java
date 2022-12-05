import java.util.concurrent.locks.ReentrantLock;

public class Synchronizer<T> {
    private final ReentrantLock lock;

    Synchronizer(ReentrantLock lock) {
        this.lock = lock;
    }

    public T execute(Operation<T> operation) {
        lock.lock();
        try {
            return operation.execute();
        } finally {
            lock.unlock();
        }
    }
}
