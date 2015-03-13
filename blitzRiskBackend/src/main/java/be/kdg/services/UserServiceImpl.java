package be.kdg.services;

import be.kdg.dao.UserDao;
import be.kdg.exceptions.DuplicateEmailException;
import be.kdg.exceptions.FriendRequestException;
import be.kdg.exceptions.DuplicateUsernameException;
import be.kdg.model.User;
import org.apache.log4j.Logger;
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
@Transactional(rollbackOn = {DuplicateEmailException.class, DuplicateUsernameException.class})
public class UserServiceImpl implements UserService, UserDetailsService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;

    //@Transactional
    @Override
    public User checkLogin(String username, String password) {
        return userDao.checkLogin(username, password);
    }

    @Override
    public User checkLoginByEmail(String mail, String password) {
        return userDao.checkLoginByEmail(mail, password);
    }

    //@Transactional
    @Override
    public void addUser(String username, String password, String email) throws DuplicateUsernameException, DuplicateEmailException {
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

    @Override
    public List<User> getFriends(String username) {
        return userDao.getFriends(username);
    }

    @Override
    public List<User> getFriendRequests(String username) {
        return userDao.getFriendRequests(username);

    }

    @Override
    public void addFriend(User requestingUser, String username) throws FriendRequestException {
        if (requestingUser.getUsername().toLowerCase().equals(username.toLowerCase())) {
            logger.warn("Can't add yourself as a friend");
            throw new FriendRequestException("Can't add yourself as a friend");
        }

        userDao.addFriend(requestingUser, userDao.loadUserByUsername(username));
    }

    @Override
    public void addFriendByEmail(User requestingUser, String email) throws FriendRequestException {
        User user = userDao.loadUserByEmail(email);
        if(user == null){
            logger.warn("User " + email + " not found");
            throw new FriendRequestException("User " + email + " not found");
        }
        userDao.addFriend(requestingUser, user);
    }

    @Override
    public void acceptFriend(User requestingUser, String usernameToAccept) throws FriendRequestException {
        userDao.acceptFriend(requestingUser, usernameToAccept);
    }

}