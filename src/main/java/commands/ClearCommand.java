package commands;


import data.User;
import utilities.CollectionManager;
import utilities.Module;

public class ClearCommand extends Command {
    private CollectionManager collectionManager;
    private User user;

    public ClearCommand(User user) {
        super("clear", "clear collection", true);
        this.user = user;
    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean execute() {
        Module.addMessage(collectionManager.clearCollection(user));
        return true;
    }
}
