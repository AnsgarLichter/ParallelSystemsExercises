import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class IndoorCleaningBoxCollection {
    private final WashParkRandomizer randomizer;

    private final List<IndoorCleaningBox> indoorCleaningBoxes = new ArrayList<IndoorCleaningBox>();

    public IndoorCleaningBoxCollection(int countOfIndoorCleaningBoxes, WashParkRandomizer randomizer) {
        this.randomizer = randomizer;

        for (int i = 0; i < countOfIndoorCleaningBoxes; i++) {
            indoorCleaningBoxes.add(new IndoorCleaningBox(this.randomizer));
        }
    }

    public void clean(Car car) {
        IndoorCleaningBox cleaningBox = this.getIndoorCleaningBox(car);
        cleaningBox.enter();
        cleaningBox.clean(car);
        cleaningBox.leave();

        this.updateAvailableIndoorCleaningBoxes();
    }

    public synchronized IndoorCleaningBox getIndoorCleaningBox(Car car) {
        while (!hasAvailableIndoorCleaningBox()) {
            System.out.println(
                    new Timestamp(System.currentTimeMillis()) + ": All washing lines are occupied. Car " + car.id
                            + " waits ...");

            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Exception occurred waiting for an available washing line: " + e.getMessage());
            }
        }

        return getAvailableIndoorCleaningBox();
    }

    public synchronized boolean hasAvailableIndoorCleaningBox() {
        return this.getAvailableIndoorCleaningBox() != null;
    }

    private synchronized IndoorCleaningBox getAvailableIndoorCleaningBox() {
        return indoorCleaningBoxes.stream()
                .filter(indoorCleaningBox -> indoorCleaningBox.isAvailable())
                .findFirst()
                .orElse(null);
    }

    private synchronized void updateAvailableIndoorCleaningBoxes() {
        notify();
    }
}
