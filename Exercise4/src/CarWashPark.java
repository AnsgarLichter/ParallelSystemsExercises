public class CarWashPark {
    private final WashParkRandomizer randomizer;

    private final WashingLineCollection washingLines;
    private final IndoorCleaningBoxCollection indoorCleaningBoxes;

    public CarWashPark(WashParkRandomizer randomizer, int countOfWashingLines, int countOfIndoorCleaningBoxes) {
        this.randomizer = randomizer;
        washingLines = new WashingLineCollection(countOfWashingLines, this.randomizer);
        indoorCleaningBoxes = new IndoorCleaningBoxCollection(countOfIndoorCleaningBoxes, this.randomizer);
    }

    public WashingLineCollection getWashingLines() {
        return this.washingLines;
    }

    public IndoorCleaningBoxCollection getIndoorCleaningBoxes() {
        return this.indoorCleaningBoxes;
    }

}