package be.kdg;



import be.kdg.model.Game;
import be.kdg.model.User;
import be.kdg.services.GameService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Alexander on 10/2/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/testcontext.xml"})
public class GameServiceTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private GameService gameService;

    @Test
    public void testGameService(){
        List<User> users = new ArrayList<User>();

        User user = new User();
        User user2 = new User();

        users.add(user);
        users.add(user2);
        Game game = gameService.createNewGame(users);

        Assert.assertEquals("", 42, game.getTerritories().size());
    }
}