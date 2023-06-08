package utilities;

import IO.ConsoleManager;

import java.sql.*;

public class DataBaseHandler {
    private String url;
    private String user;
    private String password;
    private Connection connection;

    public DataBaseHandler(String databaseHost, int databasePort, String user, String password, String databaseName) {
        this.url = "jdbc:postgresql://" + databaseHost + ":" + databasePort + "/" + databaseName;
        this.user = user;
        this.password = password;
        connectToBataBase();
    }

    public void setCommitMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(false);
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при установлении режима транзакции базы данных!");
        }
    }

    public void setNormalMode() {
        try {
            if (connection == null) throw new SQLException();
            connection.setAutoCommit(true);
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при установлении нормального режима базы данных!");
        }
    }

    public void commit() {
        try {
            if (connection == null) throw new SQLException();
            connection.commit();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при подтверждении нового состояния базы данных!");
        }
    }

    public void setSavepoint() {
        try {
            if (connection == null) throw new SQLException();
            connection.setSavepoint();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при сохранении состояния базы данных!");
        }
    }

    private void connectToBataBase() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            ConsoleManager.printError("An error occurred while connecting to the database!");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            ConsoleManager.printError("Database management driver not found((((");
        }
    }

    public PreparedStatement getPreparedStatement(String sqlStatement, boolean generateKeys) throws SQLException {
        PreparedStatement preparedStatement;
        if (connection == null) throw new SQLException();
        int autoGenerateKeys = generateKeys ? Statement.RETURN_GENERATED_KEYS : Statement.NO_GENERATED_KEYS;
        preparedStatement = connection.prepareStatement(sqlStatement, autoGenerateKeys);
        return preparedStatement;
    }

    public void closePreparedStatement(PreparedStatement sqlStatement) {
        if (sqlStatement == null) return;
        try {
            sqlStatement.close();
        } catch (SQLException e) {
        }
    }

    public void rollback() {
        try {
            if (connection == null) throw new SQLException();
            connection.rollback();
        } catch (SQLException exception) {
            System.out.println("Произошла ошибка при возврате исходного состояния базы данных!");
        }
    }

}
