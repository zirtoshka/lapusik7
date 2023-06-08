package commands;

import utilities.CollectionManager;
import utilities.Module;


public class HeadCommand extends Command {
    private CollectionManager collectionManager;

    public HeadCommand() {
        super("head", "print the first element of the collection", true);
    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute() {
        Module.addMessage(collectionManager.headOfCollection());
        return true;
    }

}
