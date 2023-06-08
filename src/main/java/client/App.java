package client;

import IO.ConsoleManager;
import IO.ScannerManager;
import data.User;
import exceptions.Disconnect;
import exceptions.IncorrectScriptException;
import exceptions.IncorrectValuesForGroupException;
import exceptions.LogOutException;
import utilities.AuthorizationManager;
import utilities.CommandManager;


import java.io.IOException;
import java.nio.channels.UnresolvedAddressException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        int port = ScannerManager.askPort();
        String host = ScannerManager.askHost();
        boolean sign;
        do {
            sign = false;
            AuthorizationManager auth = new AuthorizationManager(new Scanner(System.in));
            User user = auth.run();
            try {
                Client client = new Client(host, port, user);
                CommandManager commandManager = new CommandManager(client, user);
                try {
                    Console.run(commandManager);
                } catch (IncorrectValuesForGroupException e) {
                    ConsoleManager.printError("Wrong data");
                } catch (IncorrectScriptException e) {
                    ConsoleManager.printError("Bad script");
                } catch (LogOutException e) {
                    throw new LogOutException();
                }
            } catch (Disconnect | UnresolvedAddressException | IOException e) {
                ConsoleManager.printError(e.getMessage());
            } catch (LogOutException e) {
                sign = true;
            }
        } while (sign);
    }
}
