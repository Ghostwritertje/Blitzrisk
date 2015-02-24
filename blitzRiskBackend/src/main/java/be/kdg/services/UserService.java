package be.kdg.services;

import be.kdg.model.User;

import java.util.List;

/**
 * Created by Marlies on 17/02/2015.
 */
public interface UserService {
    //@Transactional
    User checkLogin(String username, String password);

    User checkLoginByEmail(String mail, String password);

    //@Transactional
    void addUser(String username, String password, String email);

    void addFriend(int userId, int newFriend);

    //@Transactional
    User getUser(String username);

    //@Transactional
    List<User> findall();

    void removeUser(String username);

    void changePassword(String username, String newPassword);

    void changeEmail(String username, String newEmail);

    void changeUsername(String username, String newUsername);

    public User getUserById(int id);
}
