package exceptions;

import commands.LogOutCommand;

public class LogOutException extends Exception{
    public LogOutException(){
        super("You log out, so this command is not available");
    }
}
