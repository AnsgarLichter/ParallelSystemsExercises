import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Task4 {
    private static final ExecutorService pool = Executors.newCachedThreadPool();
    private static final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    private static final WashParkRandomizer randomizer = new WashParkRandomizer();
    private static final int DURATION_OF_1_HOUR = 60000;

    public static void main(String[] args) {
        CarWashPark carWashPark = new CarWashPark(randomizer, 5, 4);
        List<Car> cars = new ArrayList<>();

        long startOfSimulation = System.currentTimeMillis();
        AtomicInteger totalCountOfCarsInCurrentHour = new AtomicInteger();
        AtomicInteger currentHour = new AtomicInteger(1);
        scheduledExecutor.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis() - startOfSimulation > DURATION_OF_1_HOUR * 3) {
                pool.shutdown();
                scheduledExecutor.shutdown();
                return;
            }

            int hourOfCurrentTime = getCurrentHour(System.currentTimeMillis() - startOfSimulation);
            if (currentHour.get() != hourOfCurrentTime) {
                totalCountOfCarsInCurrentHour.set(0);
                currentHour.set(hourOfCurrentTime);
            }

            System.out.println(
                    new Timestamp(System.currentTimeMillis()) + ": We are currently in the " + currentHour
                            + ". hour. We have in total " + totalCountOfCarsInCurrentHour + " Cars this hour.");

            int numberOfCars = getNumberOfCars(currentHour.get());
            int numberOfIndoorCleaning = getAmountOfIndoorCleaning(currentHour.get());
            for (int k = 0; k < numberOfCars; k++) {
                totalCountOfCarsInCurrentHour.getAndIncrement();
                cars.add(
                        new Car(
                                carWashPark,
                                totalCountOfCarsInCurrentHour.get() % numberOfIndoorCleaning == 0
                        )
                );
            }

            System.out.println(new Timestamp(System.currentTimeMillis()) + ": " + numberOfCars
                    + " Cars arrived at the wash park.");

            cars.forEach(pool::execute);
            cars.clear();
        }, 0, 5000, TimeUnit.MILLISECONDS);
    }

    public static int getCurrentHour(long currentTimeMillis) {
        return (int) (currentTimeMillis / DURATION_OF_1_HOUR + 1);
    }

    public static int getNumberOfCars(int hour) {
        return switch (hour) {
            case 3 -> randomizer.getNumberOfCarsForTheThirdHour();
            case 2 -> randomizer.getNumberOfCarsForTheSecondHour();
            default -> randomizer.getNumberOfCarsForTheFirstHour();
        };
    }

    public static int getAmountOfIndoorCleaning(int hour) {
        return switch (hour) {
            case 3 -> 1;
            case 2 -> 4;
            default -> 3;
        };
    }
}
