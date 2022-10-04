package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
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
    private static final String CLEAN_TABLE = "delete from User";
    private static final String GET_ALL = "from User";
    private static SessionFactory sessionFactory;
    public UserDaoHibernateImpl() {
        sessionFactory = Util.getSessionFactory();
    }


    @Override
    public void createUsersTable() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.createSQLQuery(CREATE_TABLE).addEntity(User.class).executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.createSQLQuery(DROP_TABLE).addEntity(User.class).executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (RuntimeException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.remove(session.load(User.class, id));
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            System.out.println("Пользователь с таким id не найден!");
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = null;
        List<User> users = new ArrayList<>();
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            users = session.createQuery(GET_ALL).list();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return users;
    }

    @Override
    public void cleanUsersTable() {
        Session session = null;
        try {
            session = sessionFactory.openSession();
            session.beginTransaction();
            session.createQuery(CLEAN_TABLE).executeUpdate();
            session.getTransaction().commit();
        } catch (RuntimeException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
