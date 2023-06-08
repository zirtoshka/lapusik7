package commands;

import data.User;
import utilities.CollectionManager;
import utilities.Module;

public class CheckIdCommand extends Command {
    private CollectionManager collectionManager;
    private Integer id;
    private User user;

    public CheckIdCommand(User user) {
        super("check_id", "comd for update", true);
        this.user = user;

    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean execute() {
        try {
            if (collectionManager.findById(id).getOwner().getUsername().equals(user.getUsername())) {
                if (collectionManager.findById(id) == null) {
                    Module.addMessage("No group with this id(");
                    return false;
                }
                return true;
            }
        } catch (NullPointerException e) {
            Module.addMessage("No group with this id(");
            return false;
        }
        Module.addMessage("You can't update this study group because it's not yours(");
        return false;
    }
}
