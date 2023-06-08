package commands;

import utilities.CollectionManager;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

public abstract class Command implements Serializable {
    private final String name;
    private CollectionManager collectionManager;
    private final String description;
    private final boolean isNeedCollectionManager;

    public Command(String name, String description, boolean isNeedCollectionManager) {
        this.name = name;
        this.description = description;
        this.isNeedCollectionManager=isNeedCollectionManager;
    }

    public abstract boolean execute() throws IOException;


    public String getName() {
        return name;
    }

    public boolean getIsNeedCollectionManager() {
        return isNeedCollectionManager;
    }
    public void setCollectionManager(CollectionManager collectionManager){
        this.collectionManager=collectionManager;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return name.equals(command.name) && description.equals(command.description);
    }

    @Override
    public String toString() {
        return  name+ ", description = " +
                description + "\n";
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }
}
