package utilities;

import data.User;
import exceptions.DatabaseHandlingException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static config.ConfigDataBase.*;

public class DataBaseUserManager {
    private DataBaseHandler dataBaseHandler;

    public DataBaseUserManager(DataBaseHandler dataBaseHandler) {
        this.dataBaseHandler = dataBaseHandler;
    }

    private final String SELECT_USER_BY_ID = SELECT_ALL_FROM + USER_TABLE + WHERE + USER_TABLE_ID_COLUMN + " = ?";
    private final String SELECT_USER_BY_USERNAME = SELECT_ALL_FROM + USER_TABLE +
            WHERE + USER_TABLE_USERNAME_COLUMN + " = ?";

    public User getUserByUsername(String username) throws SQLException {
        User user;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = dataBaseHandler.getPreparedStatement(SELECT_USER_BY_USERNAME, false);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User(resultSet.getString(USER_TABLE_USERNAME_COLUMN),
                        resultSet.getString(USER_TABLE_PASSWORD_COLUMN),
                        false);

            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e);

        } finally {
            dataBaseHandler.closePreparedStatement(preparedStatement);
        }
        return user;
    }

    public int getUserByUsername(User user) throws DatabaseHandlingException {
        int id;
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = dataBaseHandler.getPreparedStatement(SELECT_USER_BY_USERNAME, false);
            preparedStatement.setString(1, user.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(USER_TABLE_ID_COLUMN);
            } else {
                id = WRONG_ID;
            }
            return id;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DatabaseHandlingException();
        } finally {
            dataBaseHandler.closePreparedStatement(preparedStatement);
        }


    }


    public boolean checkUserByUsernamePassword(User user) throws DatabaseHandlingException {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = dataBaseHandler.getPreparedStatement(SELECT_USER_BY_USERNAME_AND_PASSWORD, false);
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new DatabaseHandlingException();
        } finally {
            dataBaseHandler.closePreparedStatement(preparedStatement);
        }
    }

    public boolean insertUser(User user) throws DatabaseHandlingException {
        PreparedStatement preparedStatement = null;
        try {
            if (getUserByUsername(user) != -1) return false;
            PreparedStatement preparedStatementGet = dataBaseHandler.getPreparedStatement(GET_USER_COUNT, false);
            ResultSet resultSet = preparedStatementGet.executeQuery();
            int id = 1;
            if (resultSet.next()) {
                id = resultSet.getInt("count") + 1;
            }
            preparedStatement = dataBaseHandler.getPreparedStatement(INSERT_USER, false);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            if (preparedStatement.executeUpdate() == 0) {
                throw new SQLException();
            }
            return true;
        } catch (SQLException e) {
            throw new DatabaseHandlingException();
        } finally {
            dataBaseHandler.closePreparedStatement(preparedStatement);
        }
    }

}
