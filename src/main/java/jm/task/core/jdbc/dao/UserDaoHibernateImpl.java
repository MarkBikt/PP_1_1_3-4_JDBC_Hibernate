package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private static final String CREATE_TABLE = """
                CREATE TABLE IF NOT EXISTS users (id BIGINT PRIMARY KEY AUTO_INCREMENT, 
                name VARCHAR(40) NOT NULL , 
                lastName VARCHAR(40) NOT NULL , 
                age TINYINT NOT NULL )
                """;
    private static final String DROP_TABLE = """
                DROP TABLE IF EXISTS usersdb.users
                """;
    private static final String CLEAN_TABLE = """
                TRUNCATE TABLE usersdb.users
                """;
    private static final String GET_ALL = """
                SELECT id, name, lastName, age 
                FROM usersdb.users
                """;
    private static SessionFactory sessionFactory;
    public UserDaoHibernateImpl() {
        sessionFactory = Util.getSessionFactory();
    }


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createSQLQuery(CREATE_TABLE).addEntity(User.class);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createSQLQuery(DROP_TABLE).addEntity(User.class);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            session.getTransaction().commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        }
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(session.load(User.class, id));
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            System.out.println("Пользователь с таким id не найден!");
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createSQLQuery(GET_ALL).addEntity(User.class);
            List<User> users = query.list();
            session.getTransaction().commit();
            return users;
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.createSQLQuery(CLEAN_TABLE).addEntity(User.class);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }
}
