package utilities;

import IO.ConsoleManager;
import exceptions.PropertiesException;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class PropHelper {
    private static String host;
    private static Integer port;
    private static String baseName;
    private static String user;
    private static String password;

    public static void getProperties()throws PropertiesException {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            Scanner scanner = null;
            if (os.contains("win")) {
                scanner = scan(System.getProperty("user.dir") + "\\" + "src/database.properties");
            } else {
                scanner = scan(System.getProperty("user.dir") + "/" + "src/database.properties");
            }
            fillFields(scanner);
        }catch (Exception e){
            e.printStackTrace();
            throw new PropertiesException("Failed to read database connection parameters");
        }
        if (host == null || port == null || baseName == null || user == null || password == null)
            throw new PropertiesException("Failed to read database connection parameters");
    }

    private static String[] lineParser(String line) {
        try {
            Scanner scanner = new Scanner(line);
            scanner.useDelimiter(":");
            String field = scanner.next();
            String data = scanner.next();
            return new String[]{field.trim(), data.trim()};
        } catch (NoSuchElementException e) {
            return new String[]{"", ""};
        }
    }

    private static void fillFields(Scanner scanner) {
        while (scanner.hasNext()) {
            String[] args = lineParser(scanner.next());
            switch (args[0]) {
                case ("Host"): {
                    host = args[1];
                    break;
                }
                case ("Port"): {
                    port = Integer.parseInt(args[1]);
                    break;
                }
                case ("User"): {
                    user = args[1];
                    break;
                }
                case ("Password"): {
                    password = args[1];
                    break;
                }
                case ("Basename"): {
                    baseName = args[1];
                    break;
                }
            }
        }
    }

    private static Scanner scan(String filepath) {
        Path path = Paths.get(filepath);
        Scanner scanner = null;
        while (scanner == null) {
            try {
                scanner = new Scanner(path);
            } catch (IOException e) {
                ConsoleManager.printError("database.properties file not found");
                return null;
            }
        }
        scanner.useDelimiter(System.getProperty("line.separator"));
        return scanner;
    }
    public static String getHost(){return host;}

    public static Integer getPort() {
        return port;
    }

    public static String getBaseName() {
        return baseName;
    }

    public static String getUser() {
        return user;
    }

    public static String getPassword() {
        return password;
    }
}
