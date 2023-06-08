package utilities;


import IO.ConsoleManager;
import data.Semester;
import data.StudyGroup;
import data.Person;
import data.User;
import exceptions.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static data.Semester.DEFAULT_SEMESTER;
import static data.StudyGroup.wrongId;

public class CollectionManager {
    private DataBaseCollectionManager dataBaseCollectionManager;
    private ArrayDeque<StudyGroup> studyGroupCollection = new ArrayDeque<>();
    private Set<Integer> idSet = new HashSet<>();

    private Integer newId = 1;
    private final int SIZE_EMPTY = 0;
    private final String emptyCollection = "Collection is empty";

    private static LocalDateTime lastInitTime;


    public CollectionManager(DataBaseCollectionManager dataBaseCollectionManager) {
        this.lastInitTime = null;
        this.dataBaseCollectionManager = dataBaseCollectionManager;
        loadCollection();
    }


    public ArrayDeque<StudyGroup> getStudyGroupCollection() {
        return studyGroupCollection;
    }

    public Integer generateId() {
        while (!idSet.add(newId)) {
            newId++;
        }
        return newId;
    }

    public String addToCollection(StudyGroup studyGroupFromUser) {
        if (studyGroupFromUser.getId().equals(wrongId) || idSet.add(studyGroupFromUser.getId())) {
            studyGroupFromUser.setId(generateId());
        }
        try {
            studyGroupCollection.add(dataBaseCollectionManager.insertStudyGroup(studyGroupFromUser));
            lastInitTime = LocalDateTime.now();
            return "StudyGroup added successfully";
        } catch (DatabaseHandlingException e) {
            return "Failed to add the group";
        }
    }

    public String updateById(StudyGroup studyGroup) {
        try {
            dataBaseCollectionManager.removeStudyGroupById(studyGroup.getId());
            studyGroupCollection.add(dataBaseCollectionManager.insertStudyGroup(studyGroup));
            lastInitTime = LocalDateTime.now();
            return "StudyGroup updated successfully";
        } catch (DatabaseHandlingException e) {
            return "Failed to update the group";

        }
    }

    public String addToCollectionIfMax(StudyGroup studyGroupFromUser) {
        if (studyGroupFromUser.getStudentsCount() > getMaxNumberInGroup()) {
            return addToCollection(studyGroupFromUser);
        }
        return "The StudyGroup is less than maximum.";
    }

    public String printFieldDescendingSemester() {
        return studyGroupCollection.stream()
                .map(StudyGroup::getSemesterEnum)
                .distinct()
                .filter(type -> !type.equals(DEFAULT_SEMESTER))
                .sorted(Comparator.naturalOrder())
                .map(Enum::name)
                .collect(Collectors.joining("\n"));
    }

    public String printUniqueAdmin() {
        Set<String> uniqueAdminNames = getStudyGroupCollection().stream()
                .map(StudyGroup::getGroupAdmin)
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(Person::getName, Collectors.counting()))
                .entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        return uniqueAdminNames.toString();
    }


    public String collectionType() {
        try {
            if (studyGroupCollection.isEmpty()) throw new NullCollectionException();
            return studyGroupCollection.getClass().getName();
        } catch (NullCollectionException | NullPointerException e) {
            ConsoleManager.printError(emptyCollection);
        }
        return emptyCollection;
    }

    public int collectionSize() {
        try {
            if (studyGroupCollection == null) throw new NullCollectionException();
            return studyGroupCollection.size();
        } catch (NullCollectionException e) {
            return SIZE_EMPTY;
        }

    }

    public void loadCollection() {
        studyGroupCollection = dataBaseCollectionManager.getCollection(this);

    }

    public void updateIdSet(Integer groupId) {
        idSet.add(groupId);
    }

    public String printInfo() {
        return "Collection info:\n" +
                " Type: " + collectionType() +
                "\n Quantity: " + collectionSize() +
                "\n Last enter: " + lastInitTime;
    }

    public String clearCollection(User user) {
        ArrayDeque<StudyGroup> forRemove = new ArrayDeque<>();
        try {
            studyGroupCollection.stream().filter((StudyGroup) -> StudyGroup.getOwner().getUsername().equals(user.getUsername())).forEach(forRemove::add);
            studyGroupCollection.removeAll(forRemove);
            forRemove.forEach((StudyGroup) -> {
                try {
                    dataBaseCollectionManager.removeStudyGroupById(StudyGroup.getId());
                } catch (DatabaseHandlingException e) {
                    throw new RuntimeException();
                }
            });
        } catch (NullPointerException e) {
            return "What do you want to clear? You didn't add any study groups";
        } catch (RuntimeException e) {
            return "Failed in clear command";
        }

        return "Number of deleted elements: " + forRemove.size();
    }


    public String auth(User user) throws DataBaseAuthorizationException {
        DataBaseUserManager userManager = dataBaseCollectionManager.getDataBaseUserManager();
        try {
            if (user.isSignUp()) {
                try {
                    userManager.getUserByUsername(user.getUsername());
                    throw new DataBaseAuthorizationException("This login is already exists");
                } catch (SQLException e) {
                    userManager.insertUser(user);
                    e.printStackTrace();
                    return "Registration and authorization succeeded";
                }
            } else if (!userManager.checkUserByUsernamePassword(user)) throw new DatabaseHandlingException();
        } catch (DatabaseHandlingException e) {
            throw new DataBaseAuthorizationException("Authorization declined");
        }
        return "Authorization succeeded";
    }

    public String headOfCollection() {
        try {
            if (studyGroupCollection.isEmpty()) throw new NullCollectionException();
            return studyGroupCollection.getFirst().toString();
        } catch (NullCollectionException e) {
            ConsoleManager.printError(emptyCollection);
        }
        return emptyCollection;
    }

    public StudyGroup getById(Integer id) {
        return studyGroupCollection.stream()
                .filter(studyGroup -> studyGroup.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void removeFromCollection(StudyGroup studyGroup) {
        studyGroupCollection.remove(studyGroup);
    }

    public String removeById(Integer id, User user) {
        try {
            if (findById(id) == null) throw new StudyGroupNullException();
            if (findById(id).getOwner().getUsername().equals(user.getUsername())) {
                try {
                    studyGroupCollection.remove(studyGroupCollection.stream().filter((StudyGroup) -> Objects.equals(StudyGroup.getId(), id)).findFirst().get());
                    dataBaseCollectionManager.removeStudyGroupById(id);
                    sortCollection();
                } catch (DatabaseHandlingException e) {
                    return "An error occurred when deleting an element";
                }
            } else throw new OtherOwnerException();
        } catch (StudyGroupNullException e) {
            return "No Study Group with that ID";
        } catch (OtherOwnerException e) {
            return "It's not your group, so I can't delete it";
        }
        return "The study group was removed";
    }

    public void sortCollection() {
        if (!studyGroupCollection.isEmpty()) {
            List<StudyGroup> listForSort = new ArrayList<>(studyGroupCollection);
            studyGroupCollection.clear();
            Collections.sort(listForSort, new Comparator<>() {
                @Override
                public int compare(StudyGroup o1, StudyGroup o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
            studyGroupCollection.addAll(listForSort);
        }
    }

    public StudyGroup findById(int id) {
        try {
            return studyGroupCollection.stream().filter((studyGroup) -> studyGroup.getId().equals(id)).findAny().get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public int getMaxNumberInGroup() {
        return studyGroupCollection.stream()
                .mapToInt(StudyGroup::getStudentsCount)
                .max()
                .orElse(-1);
    }

    @Override
    public String toString() {
        if (studyGroupCollection.isEmpty()) {
            return emptyCollection + "(((";
        }
        return studyGroupCollection.stream()
                .map(StudyGroup::toString)
                .collect(Collectors.joining("\n\n"));
    }


}
