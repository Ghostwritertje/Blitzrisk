package be.kdg.services;

import be.kdg.exceptions.FriendRequestException;
import be.kdg.model.User;

import java.util.List;

/**
 * Interface that defines the responsabilities of a userservice.
 */
public interface UserService {
    //@Transactional
    User checkLogin(String username, String password);

    User checkLoginByEmail(String mail, String password);

    //@Transactional
    void addUser(String username, String password, String email);

    //@Transactional
    User getUser(String username);

    //@Transactional
    List<User> findall();

    void removeUser(String username);

    void changePassword(String username, String newPassword);

    void changeEmail(String username, String newEmail);

    void changeUsername(String username, String newUsername);

    public User getUserById(int id);

    List<User> getFriends(String username);

    void addFriend(User requestingUser, String username) throws FriendRequestException;

    List<User> getFriendRequests(String username);

    void acceptFriend(User requestingUser, String usernameToAccept) throws FriendRequestException;

    void addFriendByEmail(User requestingUser, String email) throws FriendRequestException;
}
