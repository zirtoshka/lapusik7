package commands;


import utilities.HistoryWriter;
import utilities.Module;

public class HistoryCommand extends Command {
    private final HistoryWriter historyWriter;
    private final int numberOfCmd;

    public HistoryCommand(HistoryWriter historyWriter, int numberOfCmd) {
        super("history", "View last " + numberOfCmd + " commands", false);
        this.historyWriter = historyWriter;
        this.numberOfCmd = numberOfCmd;
    }

    @Override
    public boolean execute() {
        Module.addMessage(historyWriter.getHistoryExc());
        return true;
    }
}

