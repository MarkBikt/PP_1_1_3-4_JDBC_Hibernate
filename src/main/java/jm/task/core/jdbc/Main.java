package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Mark", "Bik", (byte) 28);
        userService.saveUser("Igor", "Ivanov", (byte) 33);
        userService.saveUser("Mariya", "Petrova", (byte) 23);
        userService.saveUser("Dima", "Ivanoc", (byte) 19);
        List<User> userList =userService.getAllUsers();
        for (User user : userList) {
            System.out.println(user);
        }
        userService.cleanUsersTable();
        userService.dropUsersTable();
    }
}
