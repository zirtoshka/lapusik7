package server;


import IO.ConsoleManager;
import exceptions.PropertiesException;

public class App {
    public static void main(String[] args) {
        try {
            Server server = new Server();
            while (true) {
                server.runServer();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            ConsoleManager.printError("I can't find name of file");
        } catch (PropertiesException e) {
            e.printStackTrace();

        }
    }
}