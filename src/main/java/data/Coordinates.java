package data;


import IO.ConsoleManager;
import exceptions.IncorrectValueException;
import exceptions.IncorrectValuesForGroupException;
import exceptions.NotNullException;

import java.io.Serializable;

public class Coordinates implements Serializable {
    private Double coordinatesX; //Максимальное значение поля: 576, Поле не может быть null
    public static final Double MAX_X = 576D;
    public static final Float MIN_Y = -596F;
    private Float coordinatesY; //Значение поля должно быть больше -596, Поле не может быть null
    private final Double defaultX = 0D;
    private final Float defaultY = 0F;

    public Coordinates()  {
        this.setX(defaultX);
        this.setY(defaultY);
    }

    public Coordinates(Double x, Float y) {
        this.setX(x);
        this.setY(y);
    }

    public void setX(Double coordinatesX)  {
        try {
            if (coordinatesX == null) throw new NotNullException();
            if (coordinatesX > MAX_X) throw new IncorrectValueException();
            this.coordinatesX = coordinatesX;
        } catch (IncorrectValueException e) {
            ConsoleManager.printError("This value (x) has to be less than " + MAX_X+", so I can't add the group in collection");
            this.coordinatesX=defaultX;
        } catch (NotNullException e) {
            ConsoleManager.printError("X can't be null, so I can't add the group in collection");
        }
    }

    public Double getCoordinatesX() {
        return coordinatesX;
    }

    public void setY(Float coordinatesY) {
        try {
            if (coordinatesY == null) throw new NotNullException();
            if (coordinatesY < MIN_Y) throw new IncorrectValueException();
            this.coordinatesY = coordinatesY;
        } catch (IncorrectValueException e) {
            ConsoleManager.printError("This value (y) has to be more than " + MIN_Y+", so I can't add the group in collection");
        } catch (NotNullException e) {
            ConsoleManager.printError("Y can't be null, so I can't add the group in collection");
        }
    }

    public Float getCoordinatesY() {
        return coordinatesY;
    }

    @Override
    public String toString() {
        return getCoordinatesX() + " " + getCoordinatesY();
    }
}
