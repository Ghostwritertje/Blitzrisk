package be.kdg.dao;

import be.kdg.model.User;
import org.hibernate.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Marlies on 4/01/2015.
 */

@Service("userService")
public class UserService implements UserDetailsService {

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

    public void addUser(String username, String password, String email) {
        User user = new User();
        user.setName(username);
        user.setEmail(email);
        user.setPassword(password);
        sessionFactory.getCurrentSession().save(user);
    }

    public User loadUserByUsername(String username) {
        Query query = sessionFactory.getCurrentSession().createQuery("from User where username = :username");
        query.setParameter("username", username);
        return (User) query.uniqueResult();
    }

    public List<User> findall() {
        Criteria criteria = sessionFactory.getCurrentSession().createCriteria(User.class);
        return criteria.list();
    }

    public void removeUser(String username) {
        /*Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        try {

            Query query = session.createQuery("delete from User user where user.name = :username");
            query.setParameter("username", username);
            query.executeUpdate();

        } catch (Exception e) {
            session.close();
        }
        tx.commit();*/
    }

}
