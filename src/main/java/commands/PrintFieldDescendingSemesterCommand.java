package commands;

import exceptions.NullCollectionException;
import utilities.CollectionManager;
import utilities.Module;

public class PrintFieldDescendingSemesterCommand extends Command {
    private CollectionManager collectionManager;

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public PrintFieldDescendingSemesterCommand() {
        super("print_field_descending_semester_enum", "display the values of the semesterEnum field of all elements in descending order", true);
    }

    @Override
    public boolean execute() {
        try {
            if (collectionManager.collectionSize() == 0) throw new NullCollectionException();
            Module.addMessage(collectionManager.printFieldDescendingSemester());
            return true;
        } catch (NullCollectionException e) {
            Module.addMessage("Collection is empty");
        }
        return false;
    }
}
