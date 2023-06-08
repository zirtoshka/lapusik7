package commands;

import utilities.Module;

import java.io.IOException;


public class LogOutCommand extends Command {

    public LogOutCommand() {
        super("log_out", "You log out after this cmd", true);

    }

    @Override
    public boolean execute() throws IOException {
        Module.addMessage("You've log out");
        return true;
    }
}
