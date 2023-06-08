package commands;


import utilities.Module;



public class HelpCommand extends Command {
    private String cmds;

    public HelpCommand(String cmds) {
        super("help", "display help on available commands",false);
       this.cmds=cmds+"\n"+getName()+": "+getDescription();

    }



    @Override
    public boolean execute() {
        Module.addMessage(cmds);
        return true;
    }
}
