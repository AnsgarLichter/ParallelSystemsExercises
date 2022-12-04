import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class IndoorCleaningBox {
    private final WashParkRandomizer randomizer;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private volatile boolean isAvailable;

    public IndoorCleaningBox(WashParkRandomizer randomizer) {
        this.randomizer = randomizer;
        this.isAvailable = true;
    }

    public void enter() {
        lock.lock();
        try {
            this.isAvailable = false;
        } finally {
            lock.unlock();
        }
    }

    public void leave() {
        lock.lock();
        try {
            isAvailable = true;
        } finally {
            lock.unlock();
        }
    }

    public void clean(Car car) {
        lock.lock();

        try {
            long washingTime = this.randomizer.getIndoorCleaningTime() * 1000L;
            System.out.println(new Timestamp(System.currentTimeMillis()) + ": Car " + car.id
                    + " is being cleaned for " + washingTime + "ms.");

            condition.await(washingTime, TimeUnit.MILLISECONDS);

            System.out.println(new Timestamp(System.currentTimeMillis()) + ": Car " + car.id
                    + " has been cleaned successfully.");
        } catch (Exception e) {
            System.out.println("Exception occurred washing the car: " + e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
