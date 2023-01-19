import java.sql.Timestamp;

public class IndoorCleaningBoxManager extends ResourceManager {
    private final WashParkRandomizer randomizer;

    public IndoorCleaningBoxManager(int availableResources, WashParkRandomizer randomizer) {
        super(availableResources);

        this.randomizer = randomizer;
    }

    @Override
    protected void process(Car car) {
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
    }
}
