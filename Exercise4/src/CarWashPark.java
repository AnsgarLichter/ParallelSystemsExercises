public class CarWashPark {

    private final ResourceManager washing;
    private final ResourceManager indoorCleaning;

    public CarWashPark(WashParkRandomizer randomizer, int countOfWashingLines, int countOfIndoorCleaningBoxes) {
        washing = new WashingLineManager(countOfWashingLines, randomizer);
        indoorCleaning = new IndoorCleaningBoxManager(countOfIndoorCleaningBoxes, randomizer);
    }

    public ResourceManager getWashingLines() {
        return this.washing;
    }

    public ResourceManager getIndoorCleaningBoxes() {
        return this.indoorCleaning;
    }

}