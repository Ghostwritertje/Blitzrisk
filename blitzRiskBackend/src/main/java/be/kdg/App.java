package be.kdg;

import be.kdg.model.User;
import be.kdg.dao.UserDao;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        /*Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = session.beginTransaction();
        User user = new User();
        user.setName("Joran");
        user.setEmail("jorandeboever@gmail.com");
        user.setPassword("joran");
        session.save(user);
        tx.commit();*/

        UserDao userDao = new UserDao();

        User user = userDao.getUser("Joran");
        System.out.println("User: " + user.getUsername() + ", Password: " + user.getPassword());

    }
}
