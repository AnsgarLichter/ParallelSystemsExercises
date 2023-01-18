import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Semaphore;

public class WashingLineCollection {
    private final Semaphore semaphore = new Semaphore(1);
    private final Synchronizer<WashingLine> synchronizer = new Synchronizer<>(semaphore);
    private final List<WashingLine> washingLines = new ArrayList<>();

    public WashingLineCollection(int countOfWashingLines, WashParkRandomizer randomizer) {
        for (int i = 0; i < countOfWashingLines; i++) {
            washingLines.add(new WashingLine(randomizer));
        }
    }

    public void wash(Car car) {
        WashingLine washingLine = this.getWashingLine(car);
        System.out.println("Found available washing line ... wash now");
        washingLine.enter();
        washingLine.wash(car);
        washingLine.leave();

        this.updateAvailableWashingLines();
    }

    public WashingLine getWashingLine(Car car) {
        return synchronizer.execute(() -> {
            System.out.println("Search available washing line");

            try {
                while (!hasAvailableWashingLine()) {
                    System.out.println("No washing line available ... wait");

                    System.out.println(
                            new Timestamp(System.currentTimeMillis()) + ": All washing lines are occupied. Car " + car.id
                                    + " waits ...");

                    semaphore.wait();
                    System.out.println("Wait finished");
                }

                System.out.println("Found available washing line");

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
        return washingLines.stream()
                .filter(WashingLine::isAvailable)
                .findFirst()
                .orElse(null);
    }

    private void updateAvailableWashingLines() {
        synchronizer.execute(() -> {
            System.out.println("Notify");
            semaphore.notify(); //TODO: Error current thread is not owner
            System.out.println("Notified");
            return null;
        });

    }

}
