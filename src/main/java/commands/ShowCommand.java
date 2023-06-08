package commands;


import utilities.CollectionManager;
import utilities.Module;

public class ShowCommand extends Command {
    private CollectionManager collectionManager;

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public ShowCommand() {
        super("show", "Show collection", true);
    }

    @Override
    public boolean execute() {
        Module.addMessage(collectionManager.toString());
        return true;
    }

}
