import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Task2 {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    private static final ExecutorService pool = Executors.newCachedThreadPool();
    private static final WashParkRandomizer randomizer = new WashParkRandomizer();
    private static final int DURATION_OF_1_HOUR = 60000;

    public static void main(String[] args) {
        CarWashPark carWashPark = new CarWashPark(randomizer, 5, 4);
        List<Car> cars = new ArrayList<>();

        long startOfSimulation = System.currentTimeMillis();
        int totalCountOfCarsInCurrentHour = 0;
        int currentHour = 1;
        while (System.currentTimeMillis() - startOfSimulation <= DURATION_OF_1_HOUR * 3) {
            int hourOfCurrentTime = getCurrentHour(System.currentTimeMillis() - startOfSimulation);
            if (currentHour != hourOfCurrentTime) {
                totalCountOfCarsInCurrentHour = 0;
                currentHour = hourOfCurrentTime;
            }

            System.out.println(
                    new Timestamp(System.currentTimeMillis()) + ": We are currently in the " + currentHour
                            + ". hour. We have in total " + totalCountOfCarsInCurrentHour + " Cars this hour.");

            int numberOfCars = getNumberOfCars(currentHour);
            int numberOfIndoorCleaning = getAmountOfIndoorCleaning(currentHour);

            for (int k = 0; k < numberOfCars; k++) {
                totalCountOfCarsInCurrentHour++;
                cars.add(
                        new Car(
                                carWashPark,
                                totalCountOfCarsInCurrentHour % numberOfIndoorCleaning == 0
                        )
                );
            }

            System.out.println(new Timestamp(System.currentTimeMillis()) + ": " + numberOfCars
                    + " Cars arrived at the wash park.");

            cars.forEach(pool::execute);
            cars.clear();

            lock.lock();
            try {
                condition.await(5000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                System.out.println("An exception occurred waiting for the next cars to arrive: " + e.getMessage());
            } finally {
                lock.unlock();
            }
        }

        pool.shutdown();
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
