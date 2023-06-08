package IO;

public class ConsoleManager {


    public ConsoleManager() {

    }

    public static void printInfoPurple(Object message) {
        System.out.println("\u001B[35m" + message + "\u001B[0m");
    }

    public static void printInfoPurpleBackground(Object message) {
        System.out.println("\u001B[45m" + message + "\u001B[0m");
    }
    public static void printInfoGreen(Object message){
        System.out.println("\u001B[32m"+message+"\u001B[0m");
    }
    public static void printInfoCyan(Object message){
        System.out.println("\u001B[36m"+message+"\u001B[0m");
    }

    public static void printError(Object message) {
        System.out.println("\u001B[33m" + message + "\u001B[0m");
    }


}


























