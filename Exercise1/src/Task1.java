import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Task1 {
    public static void main(String[] args) {
        WashParkRandomizer randomizer = new WashParkRandomizer();
        CarWashPark carWashPark = new CarWashPark(randomizer, 5, 4);
        List<Car> cars = new ArrayList<Car>();

        long startOfSimulation = System.currentTimeMillis();
        int totalCountOfCarsInCurrentHour = 0;
        int currentHour = 1;
        while (System.currentTimeMillis() - startOfSimulation <= 180000) {
            int hourOfCurrentTime = getCurrentHour(System.currentTimeMillis() - startOfSimulation);
            if (currentHour != hourOfCurrentTime) {
                totalCountOfCarsInCurrentHour = 0;
                currentHour = hourOfCurrentTime;
            }

            System.out.println(
                    new Timestamp(System.currentTimeMillis()) + ": We are currently in the " + currentHour
                            + ". hour. We have in total " + totalCountOfCarsInCurrentHour + " Cars this hour.");

            int numberOfCars;
            int numberOfIndoorCleaning;
            switch (currentHour) {
                case 3:
                    numberOfCars = randomizer.getNumberOfCarsForTheThirdHour();
                    numberOfIndoorCleaning = 1;
                    break;
                case 2:
                    numberOfCars = randomizer.getNumberOfCarsForTheSecondHour();
                    numberOfIndoorCleaning = 4;
                    break;
                default:
                    numberOfCars = randomizer.getNumberOfCarsForTheFirstHour();
                    numberOfIndoorCleaning = 3;
                    break;
            }

            for (int k = 0; k < numberOfCars; k++) {
                totalCountOfCarsInCurrentHour++;
                cars.add(new Car(carWashPark, totalCountOfCarsInCurrentHour % numberOfIndoorCleaning == 0));
            }

            System.out.println(new Timestamp(System.currentTimeMillis()) + ": " + numberOfCars
                    + " Cars arrived at the wash park.");

            cars.forEach(car -> car.start());
            cars.clear();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("An exception occurred waiting for the next cars to arrive: " + e.getMessage());
            }
        }
    }

    public static int getCurrentHour(long currentTimeMillis) {
        if (0 <= currentTimeMillis && currentTimeMillis < 60000) {
            return 1;
        } else if (60000 <= currentTimeMillis && currentTimeMillis < 120000) {
            return 2;
        } else if (120000 <= currentTimeMillis && currentTimeMillis <= 180000) {
            return 3;
        }

        return 0;
    }
}
