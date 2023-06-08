package commands;


import data.Person;
import data.StudyGroup;
import exceptions.ArgsException;
import exceptions.NullCollectionException;
import exceptions.StudyGroupNullException;
import utilities.CollectionManager;
import utilities.Module;

import java.util.Objects;


public class UpdateByIdCommand extends Command {

    private CollectionManager collectionManager;
    private StudyGroup argGroup;
    private Integer id;

    public UpdateByIdCommand() {
        super("update_by_id", "update element by id", true);

    }

    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setArgGroup(StudyGroup argGroup) {
        this.argGroup = argGroup;
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }

    @Override
    public boolean execute() {
        try {
            if (argGroup == null) throw new ArgsException();
            if (collectionManager.collectionSize() == 0) throw new NullCollectionException();
            StudyGroup studyGroup = collectionManager.getById(id);
            if (studyGroup == null) throw new StudyGroupNullException();
            collectionManager.removeFromCollection(studyGroup);//метод ничего не делает с множеством поэтом id можно присвоить тот же и все будет норм
            //id генерируются, поэтому будет колллекция, с новым id
            if (!Objects.equals(argGroup.getName(), StudyGroup.wrongName)) {
                studyGroup.setName(argGroup.getName());
            }
            if (!Objects.equals(argGroup.getCoordinates(), StudyGroup.wrongCoordinates)) {
                studyGroup.setCoordinates(argGroup.getCoordinates());

            }
            if (argGroup.getStudentsCount() != StudyGroup.WRONG_STUDENT_COUNT) {
                studyGroup.setStudentsCount(argGroup.getStudentsCount());

            }
            if (!(Objects.equals(argGroup.getShouldBeExpelled(), StudyGroup.wrongShouldBeExpelled))) {
                studyGroup.setShouldBeExpelled(argGroup.getShouldBeExpelled());

            }
            if (argGroup.getAverageMark() != StudyGroup.WRONG_AVERAGE_MARK) {
                studyGroup.setAverageMark(argGroup.getAverageMark());

            }
            if (!Objects.equals(argGroup.getSemesterEnum(), StudyGroup.wrongSemesterEnum)) {
                studyGroup.setSemesterEnum(argGroup.getSemesterEnum());

            }
            if (argGroup.getGroupAdmin() == null) {
                studyGroup.setGroupAdmin(argGroup.getGroupAdmin());
            } else if (!Objects.equals(argGroup.getGroupAdmin().getEyeColor(), Person.defaultEyeColor)) {
                studyGroup.setGroupAdmin(argGroup.getGroupAdmin());

            }
            collectionManager.updateById(studyGroup);
            Module.addMessage("Info was successfully updated");
            return true;
        } catch (NullCollectionException e) {
            Module.addMessage("Collection is empty");
            return false;
        } catch (StudyGroupNullException e) {
            Module.addMessage("No such Study Group with that ID");
            return false;
        } catch (ArgsException e) {
            return false;
        }
    }


}
