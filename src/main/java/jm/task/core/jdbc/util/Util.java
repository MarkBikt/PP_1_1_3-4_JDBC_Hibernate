package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


public class Util {
    private static final String URL_KEY = "db.url";
    private static final String USERNAME_KEY = "db.username";
    private static final String PASSWORD_KEY = "db.password";

    private static Connection connection;
    private static SessionFactory sessionFactory;

    private Util() {

    }
    public static Connection getConnection() {
        try {
            connection = DriverManager.getConnection(
                    PropertiesUtil.get(URL_KEY),
                    PropertiesUtil.get(USERNAME_KEY),
                    PropertiesUtil.get(PASSWORD_KEY)
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Properties prop = new Properties();
            prop.put("hibernate.connection.url", "jdbc:mysql://localhost:3306/usersdb");
            prop.put("hibernate.connection.username", "root");
            prop.put("hibernate.connection.password", "Maxmotives-092794");
            prop.put("hibernate.show_sql", "true");
            prop.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
            prop.put("hibernate.id.new_generator_mappings", "false");
            sessionFactory = new Configuration()
                    .addProperties(prop)
                    .addAnnotatedClass(User.class)
                    .buildSessionFactory();
        }
        return sessionFactory;
    }
}
