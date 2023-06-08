package utilities;

import data.*;
import exceptions.IncorrectValuesForGroupException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static data.Coordinates.MAX_X;
import static data.Coordinates.MIN_Y;
import static data.StudyGroup.wrongId;

public class GeneratorRandomData {

    private static final int LENGTH = 10;

    private static String generateRandomName() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(LENGTH);
        for (int i = 0; i < LENGTH; i++) {
            char randomChar = (char) (random.nextInt(26) + 'a');
            sb.append(randomChar);
        }
        return sb.toString();
    }


    private static <T extends Enum<?>> T getRandomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        Random random = new Random();
        int index;
        int lenEnum = values.length;
        do {
            index = random.nextInt(values.length - 1);
        } while (index == lenEnum - 1);
        return values[index];
    }


    private static <T extends Number> T generateRandomNumber(Class<T> numberClass) {
        Random random = new Random();
        if (numberClass == Double.class) {
            return numberClass.cast(random.nextDouble());
        } else if (numberClass == Integer.class) {
            return numberClass.cast(random.nextInt());
        }
        return numberClass.cast(random.nextFloat());

    }


    private static java.sql.Date generateRandomDate() {
        long startTimestamp = TimeUnit.SECONDS.toMillis(0);
        long endTimestamp = TimeUnit.SECONDS.toMillis(System.currentTimeMillis());
        long randomTimestamp = ThreadLocalRandom.current().nextLong(startTimestamp, endTimestamp);
        return new java.sql.Date(randomTimestamp);
    }

    private static int getRandomStudentsCount() {
        int count;
        do {
            count = generateRandomNumber(Integer.class);
        } while (!(count > 0));
        return count;
    }

    private static Integer getRandomShouldBeExpelled() {
        Integer count;
        do {
            count = generateRandomNumber(Integer.class);
        } while (!(count > 0));
        return count;
    }

    private static double getRandomAverageMark() {
        double mark;
        do {
            mark = generateRandomNumber(Double.class);
        } while (!(mark > 0));
        return mark;
    }

    private static Double getRandomX() {
        Double x;
        do {
            x = generateRandomNumber(Double.class);
        } while (!(x < MAX_X));
        return x;
    }

    private static Float getRandomY() {
        Float y;
        do {
            y = generateRandomNumber(Float.class);
        } while (!(y > MIN_Y));
        return y;
    }

    private static Person generateRandomAdmin() {
        return new Person(generateRandomName(),
                generateRandomDate(),
                getRandomEnum(ColorEye.class),
                getRandomEnum(ColorHair.class),
                getRandomEnum(Country.class));
    }

    private static Coordinates generateRandomCoordinates() throws IncorrectValuesForGroupException {
        return new Coordinates(getRandomX(),
                getRandomY());
    }

    public static StudyGroup generateRandomGroup() throws IncorrectValuesForGroupException {
        return new StudyGroup(wrongId,
                generateRandomName(),
                generateRandomCoordinates(),
                LocalDateTime.now(),
                getRandomStudentsCount(),
                getRandomShouldBeExpelled(),
                getRandomAverageMark(),
                getRandomEnum(Semester.class),
                generateRandomAdmin(),null);
    }

}
