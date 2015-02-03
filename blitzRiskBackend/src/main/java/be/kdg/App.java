package be.kdg;

import be.kdg.model.User;
import be.kdg.persistence.HibernateUtil;
import org.hibernate.Session;

import org.hibernate.Transaction;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        User user = new User("joran", "jorandeboever@gmail.com", "joran");
        session.save(user);

    }

}
