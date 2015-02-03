package be.kdg.model;

import be.kdg.persistence.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

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
}
