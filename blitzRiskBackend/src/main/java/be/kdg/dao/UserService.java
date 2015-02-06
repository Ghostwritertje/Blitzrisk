package be.kdg.dao;

import be.kdg.model.User;
import be.kdg.persistence.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * Created by Marlies on 4/01/2015.
 */

@Service("userService")
public class UserService {
    public User checkLogin(String username, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("from User u where u.name = :username and u.password = :password");
        query.setParameter("username", username);
        query.setParameter("password", password);
        User user = (User) query.uniqueResult();
        session.close();
        return user;
    }

    public void addUser(String username, String password, String email) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        User user = new User();
        user.setName(username);
        user.setEmail(email);
        user.setPassword(password);
        session.save(user);
        tx.commit();
    }

    public User getUser(String username) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        Query query = session.createQuery("from User user where user.name = :username");
        query.setParameter("username", username);
        User user = (User) query.uniqueResult();
        tx.commit();
        return user;
    }

    public List<User> findall() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("from User");
        List<User> users = query.list();
        session.close();
        return users;
    }

}
