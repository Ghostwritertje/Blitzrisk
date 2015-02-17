package be.kdg.services;

import be.kdg.model.User;

import java.util.List;

/**
 * Created by Marlies on 17/02/2015.
 */
public interface UserManagerServicee {
    //@Transactional
    User checkLogin(String username, String password);

    //@Transactional
    void addUser(String username, String password, String email);

    //@Transactional
    User getUser(String username);

    //@Transactional
    List<User> findall();

    void removeUser(String username);
}
