import java.util.concurrent.Semaphore;

public abstract class ResourceManager {
    private final Semaphore semaphore;

    public ResourceManager(int availableResources) {
        semaphore = new Semaphore(availableResources);
    }

    public void execute(Car car) {
        try {
            semaphore.acquire();
            process(car);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaphore.release();
        }
    }

    public boolean hasAvailableSlots() {
        return semaphore.availablePermits() > 0;
    }

    protected void process(Car car) {
    }
}
