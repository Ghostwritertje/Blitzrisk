package be.kdg.services;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Service;

/**
 * Created by user jorandeboever
 * Date:3/02/15.
 */
@Service
public class TestDataInitialisationService  implements InitializingBean {
    @Autowired
    private UserService userService;



    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            userService.addUser("Joran", "jorandeboever@gmail.com", "joran");
       userService.addUser("Marlies", "marlies@gmail.com", "marlies");
        userService.addUser("Alexander", "alexander@gmail.com", "alexander");
        userService.addUser("Valter", "valter@gmail.com", "valter");
        userService.addUser("Sven", "sven@gmail.com", "sven");
        userService.addUser("Gunther", "gunther@gmail.com", "gunther");
        }catch (Exception e){
            //nothing
        }
    }
}
