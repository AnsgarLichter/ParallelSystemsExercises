import java.sql.Timestamp;

public class WashingLine {
    private final WashParkRandomizer randomizer;
    private volatile boolean isAvailable;

    public WashingLine(WashParkRandomizer randomizer) {
        this.randomizer = randomizer;
        this.isAvailable = true;
    }

    public synchronized void enter() {
        this.isAvailable = false;
    }

    public synchronized void wash(Car car) {
        int washingTime = this.randomizer.getWashingTime() * 1000;
        System.out.println(new Timestamp(System.currentTimeMillis()) + ": Car " + car.id
                + " is being washed for " + washingTime + "ms.");

        try {
            Thread.sleep(washingTime);
        } catch (Exception e) {
            System.out.println("Exception occurred washing the car: " + e.getMessage());
        }

        System.out.println(new Timestamp(System.currentTimeMillis()) + ": Car " + car.id
                + " has been washed successfully.");
    }

    public synchronized void leave() {
        isAvailable = true;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
