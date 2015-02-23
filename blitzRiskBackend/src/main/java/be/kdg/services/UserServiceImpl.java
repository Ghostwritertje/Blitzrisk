package be.kdg.services;

import be.kdg.dao.UserDao;
import be.kdg.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Marlies on 9/02/2015.
 */
@Service("userServiceImpl")
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserDao userDao;

    //@Transactional
    @Override
    public User checkLogin(String username, String password) {
        return userDao.checkLogin(username, password);
    }

    @Override
    public User checkLoginByEmail(String mail, String password) {
        return userDao.checkLogin(mail, password);
    }

    //@Transactional
    @Override
    public void addUser(String username, String password, String email) {
        userDao.addUser(username, password, email);
    }

    //@Transactional
    @Override
     public User getUser(String username) {
        return userDao.loadUserByUsername(username);
    }

    public User getUserById(int id) {
        return userDao.loadUserById(id);
    }

    //@Transactional
    @Override
    public List<User> findall() {
        return userDao.findall();
    }

    @Override
    public void removeUser(String username) {
        userDao.removeUser(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.loadUserByUsername(username);
    }
    @Override
    public void changePassword(String username, String newPassword) {
       userDao.changePassword(username, newPassword);
    }
    @Override
    public void changeEmail(String username, String newEmail) {
        userDao.changeEmail(username, newEmail);
    }
    @Override
    public void changeUsername(String username, String newUsername) {
        userDao.changeUsername(username, newUsername);
    }
}