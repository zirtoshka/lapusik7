package data;



import IO.ConsoleManager;
import exceptions.IncorrectValuesForGroupException;
import exceptions.NotNullException;
import exceptions.WrongNameException;

import java.io.Serializable;
import java.util.Date;

import static IO.ScannerManager.patternSymbols;


public class Person implements Serializable {
    private String name; //Поле не может быть null, Строка не может быть пустой
    private final String defaultName = "default_name";
    private Date birthday; //Поле может быть null
    private final Date defaultBirthday = null;
    private ColorEye eyeColor; //Поле не может быть null
    public static final ColorEye defaultEyeColor = ColorEye.DEFAULT_COLOR;
    private ColorHair hairColor; //Поле может быть null
    private final ColorHair defaultHairColor = ColorHair.DEFAULT_COLOR;
    private Country nationality; //Поле может быть null
    private final Country defaultCountry = Country.DEFAULT_COUNTRY;
    public Person() {
        this.setName(defaultName);
        this.setBirthday(defaultBirthday);
        this.setEyeColor(defaultEyeColor);
        this.setHairColor(defaultHairColor);
        this.setNationality(defaultCountry);

    }

    public Person(String name, Date birthday, ColorEye eyeColor, ColorHair hairColor, Country nationality) {
        this.name = name;
        this.birthday = birthday;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.nationality = nationality;
    }

    public void setName(String name)  {

        try {
            if (name == null || name.isEmpty()) throw new NotNullException();
            if (!patternSymbols.matcher(name).matches()) throw new WrongNameException();
            this.name = name;
        } catch (NotNullException e) {
            ConsoleManager.printError("Name admin can't be empty, so I can't add the group in collection");

        } catch (WrongNameException e) {
            ConsoleManager.printError("I can parse only char symbol! (letters, numbers and '_'), so I can't add the group in collection");
        }
    }

    public String getName() {
        return name;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setEyeColor(ColorEye eyeColor){
        try {
            if (eyeColor == null) throw new NotNullException();
            this.eyeColor = eyeColor;
        } catch (NotNullException e) {
            ConsoleManager.printError("Eye color can't be empty, so I can't add the group in collection");
        } catch (IllegalArgumentException e) {
            ConsoleManager.printError("Hmm.. I don't know this eye color, so I can't add the group in collection");
        }
    }

    public ColorEye getEyeColor() {
        return eyeColor;
    }

    public void setHairColor(ColorHair hairColor) {
        try {
            if (hairColor == null) {
                this.hairColor = null;
                return;
            }
            this.hairColor = hairColor;
        } catch (IllegalArgumentException e) {
            ConsoleManager.printError("Hmm.. I don't know this hair color, so I can't add the group in collection");
        }
    }

    public ColorHair getHairColor() {
        return hairColor;
    }

    public void setNationality(Country nationality)  {
        try {
            if (nationality == null) {
                this.nationality = null;
                return;
            }
            this.nationality = nationality;
        } catch (IllegalArgumentException e) {
            ConsoleManager.printError("Hmm.. I don't know this country, so I can't add the group in collection");
        }
    }

    public Country getNationality() {
        return nationality;
    }

    @Override
    public String toString() {
        return "Person{" +
                "\n   name='" + name + '\'' +
                "\n   birthday=" + birthday +
                "\n   eyeColor=" + eyeColor +
                "\n   hairColor=" + hairColor +
                "\n   nationality=" + nationality +
                '}';
    }

}
