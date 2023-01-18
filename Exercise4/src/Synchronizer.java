import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Synchronizer<T> {
    private final Semaphore semaphore;

    Synchronizer(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public T execute(Operation<T> operation) {
        try {
            System.out.println("Semaphore acquire");
            semaphore.acquire();
            System.out.println("Semaphore acquired");
            return operation.execute();
        } catch (InterruptedException e) {
            System.out.println("Exception occurred trying to execute an operation: " + e.getMessage());
            return null;
        } finally {
            semaphore.release();
            System.out.println("Semaphore released");
        }
    }
}
