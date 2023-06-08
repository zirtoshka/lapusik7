package utilities;

import commands.*;
import exceptions.ExitingException;

import java.io.IOException;
import java.util.Scanner;

public class Module {

    private static CollectionManager collectionManager;
    private static String outputMessage = "";
    private static CommandManager commandManager;


    public static boolean runningCmd(Command command) throws IOException {
        String currentCmd = command.getName();
        Scanner scanner = new Scanner(currentCmd);
        scanner.useDelimiter("\\s");
        currentCmd = scanner.next();
        if (command.getIsNeedCollectionManager()) {
            command.setCollectionManager(collectionManager);
        }
        return command.execute();
    }

    public static String messageFlush() {
        String output = Module.outputMessage;
        Module.outputMessage = "";
        return output;
    }

    public static void addMessage(String msg) {
        outputMessage += msg + "\n";
    }

    public static void setCollectionManager(CollectionManager collectionManager) {
        Module.collectionManager = collectionManager;
    }

    public static CollectionManager getCollectionManager() {
        return collectionManager;
    }
}


