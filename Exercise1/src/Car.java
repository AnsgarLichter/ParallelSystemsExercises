public class Car extends Thread {
    private static int counter = 1;

    private final CarWashPark carWashPark;

    public final int id;

    private boolean needsIndoorCleaning;

    public Car(CarWashPark carWashPark, boolean needsIndoorCleaning) {
        this.carWashPark = carWashPark;
        this.id = counter++;
        this.needsIndoorCleaning = needsIndoorCleaning;
    }

    public void run() {
        if (!this.needsIndoorCleaning) {
            this.wash();
            return;
        } else if (this.carWashPark.getIndoorCleaningBoxes().hasAvailableIndoorCleaningBox()) {
            this.cleanIndoor();
            this.wash();
            return;
        }

        this.wash();
        this.cleanIndoor();
    }

    private synchronized void wash() {
        WashingLineCollection washingLines = carWashPark.getWashingLines();
        washingLines.wash(this);
    }

    private synchronized void cleanIndoor() {
        IndoorCleaningBoxCollection indoorCleaningBoxes = carWashPark.getIndoorCleaningBoxes();
        indoorCleaningBoxes.clean(this);
    }
}
