import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class WashingLineCollection {
    private final WashParkRandomizer randomizer;

    private final List<WashingLine> washingLines = new ArrayList<WashingLine>();

    public WashingLineCollection(int countOfWashingLines, WashParkRandomizer randomizer) {
        this.randomizer = randomizer;

        for (int i = 0; i < countOfWashingLines; i++) {
            washingLines.add(new WashingLine(this.randomizer));
        }
    }

    public void wash(Car car) {
        WashingLine washingLine = this.getWashingLine(car);
        washingLine.enter();
        washingLine.wash(car);
        washingLine.leave();

        this.updateAvailableWashingLines();
    }

    public synchronized WashingLine getWashingLine(Car car) {
        while (!hasAvailableWashingLine()) {
            System.out.println(
                    new Timestamp(System.currentTimeMillis()) + ": All washing lines are occupied. Car " + car.id
                            + " waits ...");

            try {
                wait();
            } catch (InterruptedException e) {
                System.out.println("Exception occurred waiting for an available washing line: " + e.getMessage());
            }
        }

        return getAvailableWashingLine();
    }

    public synchronized boolean hasAvailableWashingLine() {
        return this.getAvailableWashingLine() != null;
    }

    private synchronized WashingLine getAvailableWashingLine() {
        return washingLines.stream()
                .filter(washingLine -> washingLine.isAvailable())
                .findFirst()
                .orElse(null);

    }

    private synchronized void updateAvailableWashingLines() {
        notify();
    }

}
