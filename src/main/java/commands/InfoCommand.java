package commands;


import utilities.CollectionManager;
import utilities.Module;

public class InfoCommand extends Command {
    private CollectionManager collectionManager;

    public InfoCommand() {
        super("info", "print information about the collection", true);
    }


    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute() {
        Module.addMessage(collectionManager.printInfo());
        return true;
    }
}
