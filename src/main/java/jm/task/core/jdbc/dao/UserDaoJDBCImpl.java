package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final String CREATE_TABLE = """
                CREATE TABLE IF NOT EXISTS users (id BIGINT PRIMARY KEY AUTO_INCREMENT, 
                name VARCHAR(40) NOT NULL , 
                lastName VARCHAR(40) NOT NULL , 
                age TINYINT NOT NULL )
                """;
    private static final String DROP_TABLE = """
                DROP TABLE IF EXISTS usersdb.users
                """;
    private static final String SAVE = """
                INSERT INTO usersdb.users (name, lastName, age)
                VALUES (?, ?, ?)
                """;
    private static final String DELETE = """
                DELETE FROM usersdb.users 
                WHERE id=?
                """;
    private static final String GET_ALL = """
                SELECT * FROM usersdb.users
                """;
    private static final String CLEAN_TABLE = """
                TRUNCATE TABLE usersdb.users
                """;

    private Connection connection;
    public UserDaoJDBCImpl() {
        try {
            connection = Util.getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Не получилось создать таблицу");
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(DROP_TABLE);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Не получилось удалить таблицу");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2,lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            connection.commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE)) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        ResultSet resultSet;
        List<User> userList = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            resultSet = statement.executeQuery(GET_ALL);
            User user;
            while (resultSet.next()) {
                user = new User(resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age"));
                user.setId(resultSet.getLong("id"));
                userList.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CLEAN_TABLE);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Не получилось удалить таблицу");
        }
    }
}
