package be.kdg.dao;

import be.kdg.exceptions.FriendRequestException;
import be.kdg.model.FriendRequest;
import be.kdg.model.User;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Retrieves and saves Users in the database.
 */

@Service("userDao")
public class UserDao {
    private static final Logger logger = Logger.getLogger(UserDao.class);

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public User checkLogin(String username, String password) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User u where u.name = :username and u.password = :password");
        query.setParameter("username", username);
        query.setParameter("password", password);
        User user = (User) query.uniqueResult();
        return user;
    }

    public User checkLoginByEmail(String email, String password) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User u where u.email = :email and u.password = :password");
        query.setParameter("email", email);
        query.setParameter("password", password);
        User user = (User) query.uniqueResult();
        return user;
    }

    public void addUser(String username, String password, String email) {
        User user = new User();
        user.setName(username);
        user.setEmail(email);
        user.setPassword(password);

        try {
            sessionFactory.getCurrentSession().save(user);
        } catch (ConstraintViolationException e) {
            //throw e;
            logger.error("SQL error: "+e.getMessage());
        }
    }

    public User loadUserByUsername(String username) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User where name = :username");
        query.setParameter("username", username);
        return (User) query.uniqueResult();
    }

    public User loadUserById(int id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User where id = :id");
        query.setParameter("id", id);
        return (User) query.uniqueResult();
    }

    public List<User> findall() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
        return criteria.list();
    }

    public void removeUser(String username) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User where name = :username");
        query.setParameter("username", username);
        User user = (User) query.uniqueResult();
        sessionFactory.getCurrentSession().delete(user);
        //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!removing!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public void changePassword(String username, String newPassword) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User where name = :username");
        query.setParameter("username", username);
        User user = (User) query.uniqueResult();
        user.setPassword(newPassword);
        sessionFactory.getCurrentSession().save(user);
    }

    public void changeEmail(String username, String newEmail) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User where name = :username");
        query.setParameter("username", username);
        User user = (User) query.uniqueResult();
        user.setEmail(newEmail);
        sessionFactory.getCurrentSession().save(user);
    }

    public void changeUsername(String username, String newUsername) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User where name = :username");
        query.setParameter("username", username);
        User user = (User) query.uniqueResult();
        user.setName(newUsername);
        sessionFactory.getCurrentSession().save(user);
    }

    public void addFriend(User requestingUser, User addingUser) throws FriendRequestException {
        if(!checkFriendRequestExists(requestingUser.getUsername(), addingUser.getUsername())){
            Query query = sessionFactory.getCurrentSession().createQuery("from User user where user.name = :username");
            query.setParameter("username", addingUser.getUsername());
            User user = (User) query.uniqueResult();
            if(user == null){
                logger.warn("User doesn't exist");
                throw new FriendRequestException("User doesn't exist");
            }
            User user2 = loadUserByUsername(addingUser.getUsername());
            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setUser(requestingUser);
            friendRequest.setFriend(user2);
            user.addFriend(friendRequest);
            sessionFactory.getCurrentSession().saveOrUpdate(friendRequest);
        }else {
            logger.warn("Friendrequest already exists");
            throw new FriendRequestException("Friendrequest already exists");
        }


    }

    public List<User> getFriends(String username) {
        Query query = sessionFactory.getCurrentSession().createQuery("from FriendRequest f where f.user.name = :username and f.accepted = true ");
        query.setParameter("username", username);

        List<FriendRequest> friendRequests = query.list();
        List<User> friends = new ArrayList<>();
        for (FriendRequest friendRequest : friendRequests) {
            friends.add(friendRequest.getFriend());
        }
        query = sessionFactory.getCurrentSession().createQuery("from FriendRequest f where f.friend.name = :username and f.accepted = true ");
        query.setParameter("username", username);
        friendRequests = query.list();
        for (FriendRequest friendRequest : friendRequests) {
            friends.add(friendRequest.getUser());
        }
        return friends;

    }

    public List<User> getFriendRequests(String username) {
        Query query = sessionFactory.getCurrentSession().createQuery("from FriendRequest f where f.friend.name = :username and f.accepted = false ");
        query.setParameter("username", username);

        List<FriendRequest> friendRequests = query.list();
        List<User> friends = new ArrayList<>();
        for (FriendRequest friendRequest : friendRequests) {
            friends.add(friendRequest.getUser());
        }
        return friends;
    }

    public void acceptFriend(User requestingUser, String usernameToAccept) throws FriendRequestException {
        Query query = sessionFactory.getCurrentSession().createQuery("from FriendRequest f where f.user.name = :usernameToAccept and f.friend.name =  :username");
        query.setParameter("username", requestingUser.getUsername());
        query.setParameter("usernameToAccept", usernameToAccept);

        FriendRequest friendRequest = (FriendRequest) query.uniqueResult();
        if (friendRequest == null) {
            logger.error("Friend request not found");
            throw new FriendRequestException("Friend request not found");
        } else {
            friendRequest.setAccepted(true);
            sessionFactory.getCurrentSession().saveOrUpdate(friendRequest);
        }
    }

    private boolean checkFriendRequestExists(String requestingUsername, String usernameToAccept)  {
        Query query = sessionFactory.getCurrentSession().createQuery("from FriendRequest f where f.user.name = :usernameToAccept and f.friend.name =  :username");
        query.setParameter("username", requestingUsername);
        query.setParameter("usernameToAccept", usernameToAccept);

        FriendRequest friendRequest1 = (FriendRequest) query.uniqueResult();
         query = sessionFactory.getCurrentSession().createQuery("from FriendRequest f where f.user.name = :usernameToAccept and f.friend.name =  :username");
        query.setParameter("username", usernameToAccept);
        query.setParameter("usernameToAccept", requestingUsername);
        FriendRequest friendRequest2 = (FriendRequest) query.uniqueResult();

        return !(friendRequest1 == null && friendRequest2 == null);
    }

    public User loadUserByEmail(String email) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User where email = :email");
        query.setParameter("email", email);
        return (User) query.uniqueResult();
    }
}
