package be.kdg.services;

import be.kdg.dao.UserDao;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by user jorandeboever
 * Date:3/02/15.
 */
@Service
public class TestDataInitialisationService  implements InitializingBean {
    @Autowired
    private UserDao userDao;



    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            userDao.addUser("Joran", "jorandeboever@gmail.com", "joran");
            userDao.addUser("Marlies", "marlies@gmail.com", "marlies");
            userDao.addUser("Alexander", "alexander@gmail.com", "alexander");
            userDao.addUser("Valter", "valter@gmail.com", "valter");
            userDao.addUser("Sven", "sven@gmail.com", "sven");
            userDao.addUser("Gunther", "gunther@gmail.com", "gunther");
        }catch (Exception e){
            //nothing
        }
    }
}
