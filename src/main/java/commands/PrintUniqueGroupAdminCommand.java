package commands;


import exceptions.NullCollectionException;
import utilities.CollectionManager;
import utilities.Module;

public class PrintUniqueGroupAdminCommand extends Command {
    private CollectionManager collectionManager;

    public PrintUniqueGroupAdminCommand() {
        super("print_unique_group_admin", "display the unique values of the groupAdmin field of all items in the collection", true);
    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute() {
        try {
            if (collectionManager.collectionSize() == 0) throw new NullCollectionException();
            Module.addMessage(collectionManager.printUniqueAdmin());

        } catch (NullCollectionException e) {
            Module.addMessage("Collection is empty");
        }
        return true;

    }
}



