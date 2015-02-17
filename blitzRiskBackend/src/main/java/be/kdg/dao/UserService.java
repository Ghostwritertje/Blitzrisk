package be.kdg.dao;

import be.kdg.model.User;
import be.kdg.persistence.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Marlies on 4/01/2015.
 */

@Service("userService")
public class UserService implements UserDetailsService {
    public User checkLogin(String username, String password) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();


        Query query = session.createQuery("from User u where u.name = :username and u.password = :password");
        query.setParameter("username", username);
        query.setParameter("password", password);
        User user = (User) query.uniqueResult();

        tx.commit();
        return user;
    }

    public User checkLoginByEmail(String email, String password) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();


        Query query = session.createQuery("from User u where u.email = :email and u.password = :password");
        query.setParameter("email", email);
        query.setParameter("password", password);
        User user = (User) query.uniqueResult();

        tx.commit();
        return user;
    }

    public void addUser(String username, String password, String email) {

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        try {
            User user = new User();
            user.setName(username);
            user.setEmail(email);
            user.setPassword(password);
            session.saveOrUpdate(user);

            tx.commit();
        } catch (ConstraintViolationException e) {
            session.close();
            throw e;
        }

    }

    public User loadUserByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from User user where user.name = :username");
        query.setParameter("username", username);
        User user = (User) query.uniqueResult();

        tx.commit();
        return user;
    }

    public List<User> findall() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();

        Query query = session.createQuery("from User");
        List<User> users = query.list();

        tx.commit();
        return users;
    }

    public void removeUser(String username) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {

            Query query = session.createQuery("delete from User user where user.name = :username");
            query.setParameter("username", username);
            query.executeUpdate();

        } catch (Exception e) {
            session.close();
        }
        tx.commit();
    }

}
