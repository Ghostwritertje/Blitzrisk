package be.kdg;

import be.kdg.model.User;
import be.kdg.persistence.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collection;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        User user = new User();
        user.setName("Joran");
        user.setEmail("jorandeboever@gmail.com");
        user.setPassword("joran");
        session.save(user);
        tx.commit();

    }
}
