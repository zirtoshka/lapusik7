package commands;


import java.io.IOException;

public class ExecuteScriptCommand extends Command {
    public ExecuteScriptCommand() {
        super("execute_script", "use script from file", false);
    }


    @Override
    public boolean execute() throws IOException {
        return true;
    }
}
