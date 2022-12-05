import java.sql.Timestamp;
import java.util.concurrent.locks.ReentrantLock;

public class WashingLine {
    private final WashParkRandomizer randomizer;
    private final Synchronizer<Void> synchronizer = new Synchronizer<>(new ReentrantLock());

    private volatile boolean isAvailable;

    public WashingLine(WashParkRandomizer randomizer) {
        this.randomizer = randomizer;
        this.isAvailable = true;
    }

    public void enter() {
        synchronizer.execute(() -> {
            this.isAvailable = false;
            return null;
        });
    }

    public void wash(Car car) {
        synchronizer.execute(() -> {
            try {
                long washingTime = this.randomizer.getWashingTime() * 1000L;
                System.out.println(new Timestamp(System.currentTimeMillis()) + ": Car " + car.id
                        + " is being washed for " + washingTime + "ms.");

                Thread.sleep(washingTime);

                System.out.println(new Timestamp(System.currentTimeMillis()) + ": Car " + car.id
                        + " has been washed successfully.");
            } catch (Exception e) {
                System.out.println("Exception occurred washing the car: " + e.getMessage());
            }

            return null;
        });
    }

    public void leave() {
        synchronizer.execute(() -> {
            this.isAvailable = true;
            return null;
        });
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
