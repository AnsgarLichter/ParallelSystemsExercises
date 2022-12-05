import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class IndoorCleaningBoxCollection {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Synchronizer<IndoorCleaningBox> synchronizer = new Synchronizer<>(lock);
    private final Synchronizer<Void> voidSynchronizer = new Synchronizer<>(lock);

    private final List<IndoorCleaningBox> indoorCleaningBoxes = new ArrayList<>();

    public IndoorCleaningBoxCollection(int countOfIndoorCleaningBoxes, WashParkRandomizer randomizer) {
        for (int i = 0; i < countOfIndoorCleaningBoxes; i++) {
            indoorCleaningBoxes.add(new IndoorCleaningBox(randomizer));
        }
    }

    public void clean(Car car) {
        IndoorCleaningBox cleaningBox = this.getIndoorCleaningBox(car);
        cleaningBox.enter();
        cleaningBox.clean(car);
        cleaningBox.leave();

        this.updateAvailableIndoorCleaningBoxes();
    }

    public IndoorCleaningBox getIndoorCleaningBox(Car car) {
        return synchronizer.execute(() -> {
            try {
                while (!hasAvailableIndoorCleaningBox()) {
                    System.out.println(
                            new Timestamp(System.currentTimeMillis()) + ": All washing lines are occupied. Car " + car.id
                                    + " waits ...");

                    condition.await();
                }

                return getAvailableIndoorCleaningBox();
            } catch (InterruptedException e) {
                System.out.println("Exception occurred waiting for an available washing line: " + e.getMessage());
                return null;
            }
        });
    }

    public boolean hasAvailableIndoorCleaningBox() {
        return this.getAvailableIndoorCleaningBox() != null;
    }

    private IndoorCleaningBox getAvailableIndoorCleaningBox() {
        return synchronizer.execute(() ->
                indoorCleaningBoxes.stream()
                        .filter(IndoorCleaningBox::isAvailable)
                        .findFirst()
                        .orElse(null)
        );
    }

    private void updateAvailableIndoorCleaningBoxes() {
        voidSynchronizer.execute(() -> {
            condition.signal();
            return null;
        });
    }
}
