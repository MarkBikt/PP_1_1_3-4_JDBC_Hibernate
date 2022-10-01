package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private Util util;
    private Connection connection;
    private  Statement statement;
    public UserDaoJDBCImpl() {
        util = new Util();
        connection = util.getConnection();
        try {
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUsersTable() {
        String sgl = """
                CREATE TABLE IF NOT EXISTS users (id INT PRIMARY KEY AUTO_INCREMENT, 
                name VARCHAR(40) NOT NULL , 
                lastName VARCHAR(40) NOT NULL , 
                age TINYINT NOT NULL )
                """;
        try {
            statement.execute(sgl);
        } catch (SQLException e) {
            System.out.println("Неполучилось создать таблицу");
        }
    }

    public void dropUsersTable() {
        String sql ="DROP TABLE IF EXISTS usersdb.users";
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Не получилось удалить таблицу");
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = String .format("INSERT INTO usersdb.users SET name='%s', lastName='%s', age='%d'",
                name, lastName, age) ;

        try {
            statement.execute(sql);
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        String sql = String.format("DELETE FROM usersdb.users WHERE id=%d",id);
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        String sql = "SELECT * FROM usersdb.users";
        ResultSet resultSet;
        List<User> userList = new ArrayList<>();
        try {
            resultSet = statement.executeQuery(sql);
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
        String sql ="TRUNCATE TABLE usersdb.users";
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            System.out.println("Не получилось удалить таблицу");
        }
    }
}
