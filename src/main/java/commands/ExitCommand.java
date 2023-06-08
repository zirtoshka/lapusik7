package commands;


import utilities.Module;

public class ExitCommand extends Command {

    public ExitCommand() {
        super("exit", "finish program without saving", false);
    }

    @Override
    public boolean execute() {
        Module.addMessage("The program is ending");
        return true;
    }


}
