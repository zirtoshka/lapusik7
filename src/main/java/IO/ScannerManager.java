package IO;

import data.*;
import exceptions.*;
import utilities.CollectionManager;


import java.time.LocalDateTime;
import java.util.Date;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static config.ConfigData.inputInfo;
import static data.Coordinates.MAX_X;
import static data.Coordinates.MIN_Y;
import static data.StudyGroup.*;


public class ScannerManager {
    public static final Pattern patternSymbols = Pattern.compile("\\w*");
    public static final Pattern patternNumber = Pattern.compile("(-?)\\d+(.\\d+)?");

    public static final Pattern patterndigits = Pattern.compile("\\d*");

    private static final int MIN_PORT = 1000;
    private static final int MAX_PORT = 30000;


    public static int askPort() {
        boolean success = false;
        int port = 0;
        ConsoleManager.printInfoPurple("Enter port to connect:");
        Scanner scanner = new Scanner(System.in);
        while ((!success)) {
            try {
                port = Integer.parseInt(scanner.nextLine());
                if (!(port >= MIN_PORT && port < MAX_PORT)) {
                    throw new IncorrectValueException();
                }
                success = true;
            } catch (IncorrectValueException e) {
                ConsoleManager.printError("Incorrect  port, try again");
            }catch (NumberFormatException e){
                ConsoleManager.printError("I cant parse this to int (port)");
            }catch (NoSuchElementException e) {
                ConsoleManager.printError("Port is ctrl+D. ok, bye");
                System.exit(0);}
        }
        ConsoleManager.printInfoPurple("Your port is " + port);
        return port;
    }

    public static String askHost() {
        Scanner scanner = new Scanner(System.in);
        boolean success = false;
        String host = "";
        ConsoleManager.printInfoPurple("Enter host:");
        while (!success) {
            try {
                host = scanner.nextLine();
                if (host.isEmpty()) throw new NotNullException();
                success = true;
            } catch (NotNullException e) {
                ConsoleManager.printError("Why host is empty?");
            } catch (NoSuchElementException e) {
                ConsoleManager.printError("Host is ctrl+D. ok, bye");
                System.exit(0);}
        }
        ConsoleManager.printInfoPurple("Your host is " + host);
        return host;
    }

    public static String askCommand() {
        String command = "";
        Scanner in = new Scanner((System.in));
        while (command.equals("")) {
            ConsoleManager.printInfoPurple("Enter command: ");
            try {
                command = in.nextLine();
            }catch (NoSuchElementException e) {
                    ConsoleManager.printError("Name is ctrl+D. ok, bye");
                System.exit(0);}
        }
        return command;
    }

    public static StudyGroup askGroup(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
        return new StudyGroup(
                wrongId,
                askGroupName(runScript, scriptScanner),
                askCoordinates(runScript, scriptScanner),
                LocalDateTime.now(),
                askStudentCount(runScript, scriptScanner),
                askShouldBeExpelled(runScript, scriptScanner),
                askAverageMark(runScript, scriptScanner),
                askSemesterEnum(runScript, scriptScanner),
                askPerson(runScript, scriptScanner),null);

    }

    public static String askName(String inputTitle, String typeOfName, boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
        String name;
        Scanner scanner;
        while (true) {
            try {
                ConsoleManager.printInfoPurple(inputTitle);
                System.out.print(inputInfo);
                if (runScript) {
                    scanner = scriptScanner;
                } else {
                    scanner = new Scanner(System.in);
                }
                name = scanner.nextLine().trim();
                if (runScript) ConsoleManager.printInfoCyan(name);
                if (name.equals("")) throw new NotNullException();
                if (!patternSymbols.matcher(name).matches()) throw new WrongNameException();
                break;
            } catch (NotNullException e) {
                ConsoleManager.printError(String.format("%s can't be empty!!!", typeOfName));
                if (runScript) throw new IncorrectScriptException();
            } catch (WrongNameException e) {
                ConsoleManager.printError("I can parse only char symbol! (letters, numbers and '_')");
                if (runScript) throw new IncorrectScriptException();
            } catch (NoSuchElementException e) {
                ConsoleManager.printError("Name is ctrl+D. ok, bye");
                if (runScript) throw new IncorrectScriptException();
                System.exit(0);
            }
        }
        return name;
    }

    public static String askGroupName(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
        return askName("Enter Study Group name", "Study Group name", runScript, scriptScanner);
    }

    public static String askPersonName(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
        return askName("Enter Admin name:", "Person name", runScript, scriptScanner);
    }


    public static Double askCoordinatesX(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
        String userX;
        Double x;
        Scanner scanner;
        while (true) {
            try {
                ConsoleManager.printInfoPurple("Enter X coordinate: ");
                System.out.print(inputInfo);
                if (runScript) {
                    scanner = scriptScanner;
                } else {
                    scanner = new Scanner(System.in);
                }
                userX = scanner.nextLine().trim();
                if (userX.equals("")) throw new NotNullException();
                if (!patternNumber.matcher(userX).matches()) throw new WrongNameException();
                if (userX.indexOf(",") > -1) {
                    userX = userX.replace(",", ".");
                }
                x = Double.parseDouble(userX);
                if (x.compareTo(MAX_X)>0) throw new IncorrectValueException();
                break;
            } catch (NumberFormatException e) {
                ConsoleManager.printError("Given String is not parsable to Double");
                if (runScript) throw new IncorrectScriptException();
            } catch (NotNullException e) {
                ConsoleManager.printError("It can't be empty!!!");
                if (runScript) throw new IncorrectScriptException();
            } catch (WrongNameException e) {
                ConsoleManager.printError("hmm.. You use symbols not for numbers... why?");
                if (runScript) throw new IncorrectScriptException();
            } catch (IncorrectValueException e) {
                ConsoleManager.printError("This value has to be less than " + MAX_X);
                if (runScript) throw new IncorrectScriptException();
            } catch (NoSuchElementException e) {
                e.printStackTrace();
                ConsoleManager.printError("X is ctrl+D. ok, bye");
                if (runScript) throw new IncorrectScriptException();
                System.exit(0);
            }
        }
        return x;

    }

    public static String askArgForCmd() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static Float askCoordinatesY(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
        String userY;
        Float y;
        Scanner scanner;
        while (true) {
            try {
                ConsoleManager.printInfoPurple("Enter Y coord:");
                System.out.print(inputInfo);
                if (runScript) {
                    scanner = scriptScanner;
                } else {
                    scanner = new Scanner(System.in);
                }
                userY = scanner.nextLine().trim();
                if (userY.equals("")) throw new NotNullException();
                if (!patternNumber.matcher(userY).matches()) throw new WrongNameException();
                if (userY.contains(",")) {
                    userY = userY.replace(",", ".");
                }
                y = Float.parseFloat(userY);
                if (y < MIN_Y) throw new IncorrectValueException();
                break;
            } catch (NumberFormatException e) {
                ConsoleManager.printError("Given String is not parsable to Float");
                if (runScript) throw new IncorrectScriptException();
            } catch (NotNullException e) {
                ConsoleManager.printError("It can't be empty!!!");
                if (runScript) throw new IncorrectScriptException();
            } catch (WrongNameException e) {
                ConsoleManager.printError("hmm.. You use symbols not for numbers... why?");
                if (runScript) throw new IncorrectScriptException();
            } catch (IncorrectValueException e) {
                ConsoleManager.printError("This value has to be more than " + MIN_Y);
                if (runScript) throw new IncorrectScriptException();
            } catch (NoSuchElementException e) {
                ConsoleManager.printError("Y is ctrl+D. ok, bye");
                if (runScript) throw new IncorrectScriptException();
                System.exit(0);
            }

        }
        return y;
    }

    public static Coordinates askCoordinates(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
            Double x = askCoordinatesX(runScript, scriptScanner);
            Float y = askCoordinatesY(runScript, scriptScanner);
            Coordinates userCoordinates = new Coordinates();
            userCoordinates.setX(x);
            userCoordinates.setY(y);
            return userCoordinates;

    }

    public static int askStudentCount(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException, NumberFormatException {
        String userCount;
        int count;
        Scanner scanner;
        while (true) {
            try {
                ConsoleManager.printInfoPurple("Enter the number of students in a group:");
                System.out.print(inputInfo);
                if (runScript) {
                    scanner = scriptScanner;
                } else {
                    scanner = new Scanner(System.in);
                }
                userCount = scanner.nextLine().trim();
                if (userCount.equals("")) throw new NotNullException();
                count = Integer.parseInt(userCount);
                if (count <= 0) throw new IncorrectValueException();
                break;
            } catch (NotNullException | IncorrectValueException e) {
                ConsoleManager.printError("Are you sure it could be the number of students??");
                if (runScript) throw new IncorrectScriptException();
            } catch (NumberFormatException e) {
                ConsoleManager.printError("Given String is not parsable to int");
            } catch (NoSuchElementException e) {
                ConsoleManager.printError("The number of students is ctrl+D. ok, bye");
                if (runScript) throw new IncorrectScriptException();
                System.exit(0);
            }
        }
        return count;
    }

    public static Integer askShouldBeExpelled(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException, NumberFormatException {
        String userCountExpelled;
        Integer countExpelled;
        Scanner scanner;
        while (true) {
            try {
                ConsoleManager.printInfoPurple("Enter the number of students to be expelled:");
                System.out.print(inputInfo);
                if (runScript) {
                    scanner = scriptScanner;
                } else {
                    scanner = new Scanner(System.in);
                }
                userCountExpelled = scanner.nextLine().trim();
                if (userCountExpelled.equals("")) throw new NotNullException();
                countExpelled = Integer.parseInt(userCountExpelled);
                if (countExpelled <= 0) throw new IncorrectValueException();
                break;
            } catch (NotNullException e) {
                return null;
            } catch (IncorrectValueException e) {
                ConsoleManager.printError("It has to be more than 0");
                if (runScript) throw new IncorrectScriptException();
            } catch (NumberFormatException e) {
                ConsoleManager.printError("Given String is not parsable to Integer");
                if (runScript) throw new IncorrectScriptException();
            } catch (NoSuchElementException e) {
                ConsoleManager.printError("The number of students to be expelled is ctrl+D. ok, bye");
                if (runScript) throw new IncorrectScriptException();
                System.exit(0);
            }
        }
        return countExpelled;
    }

    public static double askAverageMark(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
        String userMark;
        double countMark;
        Scanner scanner;
        while (true) {
            try {
                ConsoleManager.printInfoPurple("Enter average mark:");
                System.out.print(inputInfo);
                if (runScript) {
                    scanner = scriptScanner;
                } else {
                    scanner = new Scanner(System.in);
                }
                userMark = scanner.nextLine().trim();
                countMark = Double.parseDouble(userMark);
                if (countMark <= 0) throw new IncorrectValueException();
                break;
            } catch (IncorrectValueException e) {
                ConsoleManager.printError("It has to be more than 0");
                if (runScript) throw new IncorrectScriptException();
            } catch (NumberFormatException e) {
                ConsoleManager.printError("Given String is not parsable to double");
                if (runScript) throw new IncorrectScriptException();
            } catch (NoSuchElementException e) {
                ConsoleManager.printError("Average mark is ctrl+D. ok, bye");
                if (runScript) throw new IncorrectScriptException();
                System.exit(0);
            }
        }
        return countMark;
    }

    private static <T extends Enum<T>> T askEnum(T[] values, boolean runScript, Scanner scriptScanner, String nameEnum, String list) throws IncorrectScriptException {
        String userEnum;
        Integer enumId;
        T enumRes = null;
        Scanner scanner;
        while (true) {
            try {
                ConsoleManager.printInfoPurple(nameEnum + "list - " + list);
                ConsoleManager.printInfoPurple("Enter your " + nameEnum + ":");
                System.out.print(inputInfo);
                if (runScript) {
                    scanner = scriptScanner;
                } else {
                    scanner = new Scanner(System.in);
                }
                userEnum = scanner.nextLine().trim();
                if (userEnum.equals("")) {
                    throw new NotNullException();
                }
                try {
                    enumId = Integer.parseInt(userEnum);
                    if (enumId >= values.length -1 || enumId < 0) {
                        throw new IncorrectIndexInOrdinalEnumException();
                    }
                    enumRes = values[enumId];
                    break;
                } catch (NumberFormatException e) {
                    for (T en : values) {
                        if (en.toString().equals(userEnum.toUpperCase())) {
                            enumRes = en;
                            break;
                        }
                    }
                    if (enumRes == null) throw new IllegalArgumentException();
                    break;
                } catch (IncorrectIndexInOrdinalEnumException e) {
                    ConsoleManager.printError("I don't know this "+nameEnum);
                    if (runScript) throw new IncorrectScriptException();
                }

            } catch (NotNullException e) {
                ConsoleManager.printError("It can't be empty!!");
                if (runScript) throw new IncorrectScriptException();
            } catch (IllegalArgumentException e) {
                ConsoleManager.printError("I don't know this "+nameEnum);
                if (runScript) throw new IncorrectScriptException();
            } catch (NoSuchElementException e) {
                ConsoleManager.printError(nameEnum+" is ctrl+D. ok, bye");
                if (runScript) throw new IncorrectScriptException();
                System.exit(0);
            }
        }
        ConsoleManager.printInfoPurpleBackground(enumRes);
        return enumRes;
    }

    public static Semester askSemesterEnum(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
        return askEnum(Semester.values(), runScript, scriptScanner, "semester", Semester.getList());
    }
    public static ColorEye askEyeColor(boolean runScript,Scanner scriptScanner) throws IncorrectScriptException{
        return askEnum(ColorEye.values(),runScript, scriptScanner,"color", ColorEye.getList());
    }
    public static Country askNationality(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
        return askEnum(Country.values(), runScript, scriptScanner, "country", Country.getList());
    }
    public static ColorHair askHairColor(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
        return askEnum(ColorHair.values(),runScript,scriptScanner,"color",ColorHair.getList());
    }

    public static Person askPerson(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
        if (askQuestion("Is there an admin?", runScript, scriptScanner)) {
            return new Person(askPersonName(runScript, scriptScanner), askBirthday(runScript, scriptScanner),
                    askEyeColor(runScript, scriptScanner), askHairColor(runScript, scriptScanner), askNationality(runScript, scriptScanner));
        }
        return null;
    }

    public static Date askBirthday(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
        String userDate;
        Date date;
        Scanner scanner;
        while (true) {
            try {
                ConsoleManager.printInfoPurple("You can use formats: 'January 19, 1970', '01/19/1970'");
                ConsoleManager.printInfoPurple("Enter your birthday for admin: ");
                System.out.print(inputInfo);
                if (runScript) {
                    scanner = scriptScanner;
                } else {
                    scanner = new Scanner(System.in);
                }
                userDate = scanner.nextLine().trim();
                if (userDate.equals("")) throw new NotNullException();
                date = new Date(userDate);
                break;
            } catch (NotNullException e) {
                return null;
            } catch (IllegalArgumentException e) {
                ConsoleManager.printError("You use a very strange format");
                if (runScript) throw new IncorrectScriptException();
            } catch (NoSuchElementException e) {
                ConsoleManager.printError("Birthday is ctrl+D. ok, bye");
                if (runScript) throw new IncorrectScriptException();
                System.exit(0);
            }
        }

        return date;
    }


    public static StudyGroup askQuestionForUpdate(boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
        String name = wrongName;
        Coordinates coordinates = wrongCoordinates;
        LocalDateTime creationDate = LocalDateTime.now();
        int studentsCount = WRONG_STUDENT_COUNT;
        Integer shouldBeExpelled = wrongShouldBeExpelled;
        double averageMark = WRONG_AVERAGE_MARK;
        Semester semesterEnum = wrongSemesterEnum;
        Person groupAdmin = new Person();
        if (askQuestion("Change study group name?", runScript, scriptScanner)) {
            name = askGroupName(runScript, scriptScanner);
        }
        if (askQuestion("Change study group coordinates?", runScript, scriptScanner)) {
            coordinates = askCoordinates(runScript, scriptScanner);
        }
        if (askQuestion("Change the number of students in a group??", runScript, scriptScanner)) {
            studentsCount = askStudentCount(runScript, scriptScanner);
        }
        if (askQuestion("Change the number of students to be expelled??", runScript, scriptScanner)) {
            shouldBeExpelled = askShouldBeExpelled(runScript, scriptScanner);
        }
        if (askQuestion("Change study group average mark?", runScript, scriptScanner)) {
            averageMark = askAverageMark(runScript, scriptScanner);
        }
        if (askQuestion("Change study group semester?", runScript, scriptScanner)) {
            semesterEnum = askSemesterEnum(runScript, scriptScanner);
        }
        if (askQuestion("Change study group admin?", runScript, scriptScanner)) {
            groupAdmin = askPerson(runScript, scriptScanner);
        }
        return new StudyGroup(wrongId, name, coordinates, creationDate, studentsCount,
                shouldBeExpelled, averageMark, semesterEnum, groupAdmin,null);

    }

    public static boolean askQuestion(String question, boolean runScript, Scanner scriptScanner) throws IncorrectScriptException {
        String finalQuestion = question + " (+/-):";
        String answer;
        Scanner scanner;
        while (true) {
            try {
                ConsoleManager.printInfoPurple(finalQuestion);
                System.out.println(inputInfo);
                if (runScript) {
                    scanner = scriptScanner;
                } else {
                    scanner = new Scanner(System.in);
                }
                answer = scanner.nextLine().trim();
                if (answer.equals("")) throw new NotNullException();
                if (!(answer.equals("+") || answer.equals("-"))) throw new IncorrectValueException();
                break;
            } catch (NotNullException e) {
                System.out.println("I know that silence is golden. But what should I do with it? I only understand + and -");
                if (runScript) throw new IncorrectScriptException();
            } catch (IncorrectValueException e) {
                System.out.println("I believed that you are a smart person and able to distinguish other characters from +/-");
                if (runScript) throw new IncorrectScriptException();
            } catch (NoSuchElementException e) {
                System.out.println("Answer is ctrl+D. ok, bye");
                if (runScript) throw new IncorrectScriptException();
                System.exit(0);
            }
        }
        return answer.equals("+");
    }
}
