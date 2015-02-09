package be.kdg;

import be.kdg.model.User;
import be.kdg.dao.UserService;
import be.kdg.persistence.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;


/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
    /*
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        User user = new User();
        user.setName("Joran");
        user.setEmail("jorandeboever@gmail.com");
        user.setPassword("joran");
        session.save(user);
        tx.commit();




        User user = userService.loadUserByUsername("Joran");

        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        session.delete(user);
        tx.commit();
        */
        UserService userService = new UserService();
        userService.removeUser("testuser");
        userService.removeUser("testuser2");
        userService.addUser("testuser", "testuserpass", "testuser@test.be");

        System.out.println("finished!");
    }
}
