import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class WashingLineCollection {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private final Synchronizer<WashingLine> synchronizer = new Synchronizer<>(lock);
    private final Synchronizer<Void> voidSynchronizer = new Synchronizer<>(lock);
    private final List<WashingLine> washingLines = new ArrayList<>();

    public WashingLineCollection(int countOfWashingLines, WashParkRandomizer randomizer) {
        for (int i = 0; i < countOfWashingLines; i++) {
            washingLines.add(new WashingLine(randomizer));
        }
    }

    public void wash(Car car) {
        WashingLine washingLine = this.getWashingLine(car);
        washingLine.enter();
        washingLine.wash(car);
        washingLine.leave();

        this.updateAvailableWashingLines();
    }

    public WashingLine getWashingLine(Car car) {
        return synchronizer.execute(() -> {
            try {
                while (!hasAvailableWashingLine()) {
                    System.out.println(
                            new Timestamp(System.currentTimeMillis()) + ": All washing lines are occupied. Car " + car.id
                                    + " waits ...");

                    condition.await();
                }

                return getAvailableWashingLine();
            } catch (InterruptedException e) {
                System.out.println("Exception occurred waiting for an available washing line: " + e.getMessage());
                return null;
            }
        });
    }

    public boolean hasAvailableWashingLine() {
        return this.getAvailableWashingLine() != null;
    }

    private WashingLine getAvailableWashingLine() {
        return synchronizer.execute(() -> washingLines.stream()
                .filter(WashingLine::isAvailable)
                .findFirst()
                .orElse(null)
        );
    }

    private void updateAvailableWashingLines() {
        voidSynchronizer.execute(() -> {
            condition.signal();
            return null;
        });
    }

}
