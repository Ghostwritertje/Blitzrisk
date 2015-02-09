package be.kdg.services;

import be.kdg.dao.UserDao;
import be.kdg.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Marlies on 9/02/2015.
 */
@Service
public class UserManagerService {
    @Autowired
    private UserDao userDao;

    @Transactional
    public User checkLogin(String username, String password) {
        return userDao.checkLogin(username, password);
    }

    @Transactional
    public void addUser(String username, String password, String email) {
        userDao.addUser(username, password, email);
    }

    @Transactional
    public User getUser(String username) {
        return userDao.getUser(username);
    }

    @Transactional
    public List<User> findall() {
        return userDao.findall();
    }
}
