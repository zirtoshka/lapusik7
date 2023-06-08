package utilities;

import data.*;
import exceptions.DatabaseHandlingException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayDeque;

import static config.ConfigDataBase.*;

public class DataBaseCollectionManager {
    private DataBaseHandler dataBaseHandler;
    private DataBaseUserManager dataBaseUserManager;

    public DataBaseCollectionManager(DataBaseHandler dataBaseHandler, DataBaseUserManager dataBaseUserManager) {
        this.dataBaseHandler = dataBaseHandler;
        this.dataBaseUserManager = dataBaseUserManager;
    }

    private StudyGroup createStudyGroup(ResultSet resultSet) {
        try {
            Integer id = resultSet.getInt(STUDY_GROUP_TABLE_ID_COLUMN);
            String name = resultSet.getString(STUDY_GROUP_TABLE_NAME_COLUMN);
            Coordinates coordinates = new Coordinates(resultSet.getDouble(STUDY_GROUP_TABLE_X_COLUMN), resultSet.getFloat(STUDY_GROUP_TABLE_Y_COLUMN));
            LocalDateTime creationTime = resultSet.getTimestamp(STUDY_GROUP_TABLE_CREATION_DATE_COLUMN).toLocalDateTime();
            int studentCount = resultSet.getInt(STUDY_GROUP_TABLE_STUDENT_COUNT_COLUMN);
            Integer shouldBeExpelled = resultSet.getInt(STUDY_GROUP_TABLE_SHOULD_BE_EXPELLED_COLUMN);
            double averageMark = resultSet.getDouble(STUDY_GROUP_TABLE_AVERAGE_MARK_COLUMN);
            Semester semester = Semester.valueOf(resultSet.getObject(STUDY_GROUP_TABLE_SEMESTER_COLUMN).toString());
            String nameAdmin = resultSet.getString(GROUP_ADMIN_TABLE_NAME_COLUMN);
            Person admin = null;
            if (nameAdmin != null) {
                admin = new Person(nameAdmin, resultSet.getTime(GROUP_ADMIN_TABLE_BIRTHDAY_COLUMN),
                        ColorEye.valueOf(resultSet.getObject(GROUP_ADMIN_TABLE_EYE_COLOR_COLUMN).toString()),
                        ColorHair.valueOf(resultSet.getObject(GROUP_ADMIN_TABLE_HAIR_COLOR_COLUMN).toString()),
                        Country.valueOf(resultSet.getObject(GROUP_ADMIN_TABLE_NATIONALITY_COLUMN).toString()));
            }

            User owner = new User(resultSet.getString(USER_TABLE_USERNAME_COLUMN), resultSet.getString(USER_TABLE_PASSWORD_COLUMN), true);

            return new StudyGroup(id,
                    name,
                    coordinates,
                    creationTime,
                    studentCount,
                    shouldBeExpelled,
                    averageMark,
                    semester,
                    admin, owner);


        } catch (SQLException e) {
            return null;
        }
    }

    public ArrayDeque<StudyGroup> getCollection(CollectionManager collectionManager) {
        ArrayDeque<StudyGroup> studyGroups = new ArrayDeque<>();
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = dataBaseHandler.getPreparedStatement(SELECT_ALL_STUDY_GROUPS, false);
            if (preparedStatement == null) {
                return studyGroups;
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                StudyGroup studyGroupFromDatabase = createStudyGroup(resultSet);
                studyGroups.add(studyGroupFromDatabase);
                collectionManager.updateIdSet(studyGroupFromDatabase.getId());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dataBaseHandler.closePreparedStatement(preparedStatement);
        }
        return studyGroups;


    }

    public DataBaseUserManager getDataBaseUserManager() {
        return dataBaseUserManager;
    }

    public void removeStudyGroupById(Integer id) throws DatabaseHandlingException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = dataBaseHandler.getPreparedStatement(DELETE_MOVIE_BY_ID, false);
            preparedStatement.setInt(1, id);
            if (preparedStatement.executeUpdate() == 0) throw new DatabaseHandlingException();
        } catch (SQLException e) {
            throw new DatabaseHandlingException();
        } finally {
            dataBaseHandler.closePreparedStatement(preparedStatement);
        }
    }

    public StudyGroup insertStudyGroup(StudyGroup studyGroup) throws DatabaseHandlingException {
        PreparedStatement preparedStatement = null;
        try {
            dataBaseHandler.setCommitMode();
            dataBaseHandler.setSavepoint();
            preparedStatement = dataBaseHandler.getPreparedStatement(INSERT_STUDY_GROUP, true);
            preparedStatement.setInt(1, studyGroup.getId());
            preparedStatement.setString(2, studyGroup.getName());
            preparedStatement.setDouble(3, studyGroup.getCoordinates().getCoordinatesX());
            preparedStatement.setFloat(4, studyGroup.getCoordinates().getCoordinatesY());
            preparedStatement.setObject(5, studyGroup.getCreationDate());
            preparedStatement.setInt(6, studyGroup.getStudentsCount());
            preparedStatement.setInt(7, studyGroup.getShouldBeExpelled());
            preparedStatement.setDouble(8, studyGroup.getAverageMark());
            preparedStatement.setObject(9, studyGroup.getSemesterEnum(), Types.OTHER);
            preparedStatement.setObject(10, studyGroup.getOwner().getUsername());
            preparedStatement.executeUpdate();

            if (studyGroup.getGroupAdmin() != null) {
                preparedStatement = dataBaseHandler.getPreparedStatement(INSERT_GROUP_ADMIN, true);
                preparedStatement.setInt(1, studyGroup.getId());
                preparedStatement.setString(2, studyGroup.getGroupAdmin().getName());
                preparedStatement.setObject(3, studyGroup.getGroupAdmin().getBirthday(), Types.TIMESTAMP);
                preparedStatement.setObject(4, studyGroup.getGroupAdmin().getEyeColor(), Types.OTHER);
                preparedStatement.setObject(5, studyGroup.getGroupAdmin().getHairColor(), Types.OTHER);
                preparedStatement.setObject(6, studyGroup.getGroupAdmin().getNationality(), Types.OTHER);
                preparedStatement.executeUpdate();
            }
            preparedStatement.close();
            dataBaseHandler.commit();
            return studyGroup;

        } catch (SQLException e) {
            e.printStackTrace();
            dataBaseHandler.rollback();
            throw new DatabaseHandlingException();
        } finally {
            dataBaseHandler.closePreparedStatement(preparedStatement);
            dataBaseHandler.setNormalMode();
        }
    }
}
