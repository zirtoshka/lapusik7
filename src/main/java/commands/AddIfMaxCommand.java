package commands;


import data.StudyGroup;
import utilities.CollectionManager;
import utilities.Module;

public class AddIfMaxCommand extends Command {
    private CollectionManager collectionManager;
    private StudyGroup studyGroup;

    public AddIfMaxCommand() {
        super("add_if_max", "add a new element if the number of students in the new group is more than in the others", true);
    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public void setArgGroup(StudyGroup studyGroup) {
        this.studyGroup = studyGroup;
    }


    @Override
    public boolean execute() {
        Module.addMessage(collectionManager.addToCollectionIfMax(studyGroup));
        return true;
    }
}

