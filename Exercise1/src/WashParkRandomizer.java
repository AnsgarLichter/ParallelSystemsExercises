import java.util.Random;

public class WashParkRandomizer {
    private final Random randomizer = new Random();

    public int getWashingTime() {
        return randomizer.nextInt(6) + 5;
    }

    public int getIndoorCleaningTime() {
        return (randomizer.nextInt(3) + 1) * 5;
    }

    public int getNumberOfCarsForTheFirstHour() {
        return randomizer.nextInt(3) + 3;
    }

    public int getNumberOfCarsForTheSecondHour() {
        return randomizer.nextInt(4) + 4;
    }

    public int getNumberOfCarsForTheThirdHour() {
        return randomizer.nextInt(3) + 3;
    }
}
