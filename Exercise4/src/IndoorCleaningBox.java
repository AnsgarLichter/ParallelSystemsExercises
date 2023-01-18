import java.sql.Timestamp;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class IndoorCleaningBox {
    private final WashParkRandomizer randomizer;
    private final Synchronizer<Void> synchronizer = new Synchronizer<>(new Semaphore(1));

    private volatile boolean isAvailable;

    public IndoorCleaningBox(WashParkRandomizer randomizer) {
        this.randomizer = randomizer;
        this.isAvailable = true;
    }

    public void enter() {
        synchronizer.execute(() -> {
            this.isAvailable = false;
            return null;
        });
    }

    public void leave() {
        synchronizer.execute(() -> {
            this.isAvailable = true;
            return null;
        });
    }

    public void clean(Car car) {
        synchronizer.execute(() -> {
            try {
                long indoorCleaningTime = this.randomizer.getIndoorCleaningTime() * 1000L;
                System.out.println(new Timestamp(System.currentTimeMillis()) + ": Car " + car.id
                        + " is being cleaned for " + indoorCleaningTime + "ms.");

                Thread.sleep(indoorCleaningTime);

                System.out.println(new Timestamp(System.currentTimeMillis()) + ": Car " + car.id
                        + " has been cleaned successfully.");
            } catch (InterruptedException e) {
                System.out.println("Exception occurred washing the car: " + e.getMessage());
            }

            return null;
        });
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
