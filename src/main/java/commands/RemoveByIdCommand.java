package commands;


import data.User;
import utilities.CollectionManager;
import utilities.Module;

public class RemoveByIdCommand extends Command {
    private CollectionManager collectionManager;
    private Integer argId;
    private User user;

    public RemoveByIdCommand(User user) {
        super("remove_by_id", "remove element by id", true);
        this.user = user;

    }

    public void setArgId(Integer argId) {
        this.argId = argId;
    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute() {
        Module.addMessage(collectionManager.removeById(argId, user));
        return true;
    }
}
