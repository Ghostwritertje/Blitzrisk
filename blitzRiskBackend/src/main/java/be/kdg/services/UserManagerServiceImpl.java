package be.kdg.services;

import be.kdg.dao.UserService;
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
@Service("userManagerService")
@Transactional
public class UserManagerServiceImpl implements UserManagerService, UserDetailsService {

    @Autowired
    private UserService userService;

    //@Transactional
    @Override
    public User checkLogin(String username, String password) {
        return userService.checkLogin(username, password);
    }

    @Override
    public User checkLoginByEmail(String mail, String password) {
        return userService.checkLogin(mail, password);
    }

    //@Transactional
    @Override
    public void addUser(String username, String password, String email) {
        userService.addUser(username, password, email);
    }

    //@Transactional
    @Override
    public User getUser(String username) {
        return userService.loadUserByUsername(username);
    }

    //@Transactional
    @Override
    public List<User> findall() {
        return userService.findall();
    }

    @Override
    public void removeUser(String username) {
        userService.removeUser(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.loadUserByUsername(username);
    }
}