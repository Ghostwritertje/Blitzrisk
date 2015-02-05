package be.kdg.services;

import be.kdg.model.User;
import be.kdg.persistence.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Marlies on 4/01/2015.
 */

@Service("userService")
public class UserService {
    public be.kdg.model.User checkLogin(String username, String password) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Query query = session.createQuery("from be.kdg.model.User u where u.name=:username and u.password=:password");
            query.setParameter("username", username);
            query.setParameter("password", password);
            User user = (User) query.uniqueResult();
            session.close();
            return user;
    }

    public void addUser(String username, String email, String password){
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        User user = new User();
        user.setName(username);
        user.setEmail(email);
        user.setPassword(password);
        session.save(user);
        tx.commit();
    }

    public List<User> findall() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Query query = session.createQuery("select u.name, u.email from be.kdg.model.User u");
        List<User> users = query.list();
        session.close();
        return users;
    }

}
