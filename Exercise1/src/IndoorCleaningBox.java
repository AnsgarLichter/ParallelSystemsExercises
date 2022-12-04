import java.sql.Timestamp;

public class IndoorCleaningBox {
    private final WashParkRandomizer randomizer;
    private volatile boolean isAvailable;

    public IndoorCleaningBox(WashParkRandomizer randomizer) {
        this.randomizer = randomizer;
        this.isAvailable = true;
    }

    public synchronized void enter() {
        this.isAvailable = false;
    }

    public synchronized void leave() {
        isAvailable = true;
    }

    public synchronized void clean(Car car) {
        int washingTime = this.randomizer.getIndoorCleaningTime() * 1000;
        System.out.println(new Timestamp(System.currentTimeMillis()) + ": Car " + car.id
                + " is being cleaned for " + washingTime + "ms.");

        try {
            Thread.sleep(washingTime);
        } catch (Exception e) {
            System.out.println("Exception occurred washing the car: " + e.getMessage());
        }

        System.out.println(new Timestamp(System.currentTimeMillis()) + ": Car " + car.id
                + " has been cleaned successfully.");
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
