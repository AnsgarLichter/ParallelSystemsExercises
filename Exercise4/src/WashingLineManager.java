import java.sql.Timestamp;

public class WashingLineManager extends ResourceManager {
    private final WashParkRandomizer randomizer;

    public WashingLineManager(int availableResources, WashParkRandomizer randomizer) {
        super(availableResources);

        this.randomizer = randomizer;
    }

    @Override
    protected void process(Car car) {
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
    }
}
