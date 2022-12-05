public class Car implements Runnable {
    private static int counter = 1;

    public final int id;


    private final CarWashPark carWashPark;
    private final boolean needsIndoorCleaning;

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

    private void wash() {
        WashingLineCollection washingLines = carWashPark.getWashingLines();
        washingLines.wash(this);
    }

    private void cleanIndoor() {
        IndoorCleaningBoxCollection indoorCleaningBoxes = carWashPark.getIndoorCleaningBoxes();
        indoorCleaningBoxes.clean(this);
    }
}
