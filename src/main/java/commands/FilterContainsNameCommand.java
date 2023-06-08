package commands;

import data.StudyGroup;
import exceptions.NullCollectionException;
import utilities.CollectionManager;
import utilities.Module;

public class FilterContainsNameCommand extends Command {
    private CollectionManager collectionManager;
    private String name;

    public FilterContainsNameCommand() {
        super("filter_contains_name", "display elements whose name field value contains the given substring", true);
    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean execute() {
        try {
            if (collectionManager.collectionSize() == 0) throw new NullCollectionException();
            boolean isDone = false;
            for (StudyGroup sg : collectionManager.getStudyGroupCollection()
            ) {
                if (sg.getName().contains(name)) {
                    Module.addMessage(sg.getName());
                    isDone = true;
                }
            }
            if (!isDone) {
                Module.addMessage("I can't find smth with " + name);
            }
            return true;
        } catch (NullCollectionException e) {
            Module.addMessage("Collection is empty");
        }
        return false;
    }
}
