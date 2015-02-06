package be.kdg;

import be.kdg.model.User;
import be.kdg.dao.UserService;

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

        UserService userService = new UserService();

        User user = userService.getUser("Joran");
        System.out.println("User: " + user.getUsername() + ", Password: " + user.getPassword());

    }
}
