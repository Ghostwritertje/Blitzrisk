package be.kdg;



import be.kdg.model.Game;
import be.kdg.model.Territory;
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
import java.util.List;

import static org.junit.Assert.assertTrue;

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
    public void dividableBy4Players() {
        List<User> users = new ArrayList<User>();

        User user = new User();
        user.setName("een");
        User user2 = new User();
        user2.setName("twee");
        User user3 = new User();
        user3.setName("drie");
        User user4 = new User();
        user4.setName("vier");

        users.add(user);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        //4 users use 40 territories
        Game game = gameService.createNewGame(users);
        int numberOfTerritories = 0;
        for (Territory territory: game.getTerritories()) {
            if(territory.getPlayer() != null) numberOfTerritories++;
        }
        assertTrue("4 players use 40 territories", 40 == numberOfTerritories);
    }

    @Test
    public void dividableBy3Players() {
        List<User> users = new ArrayList<User>();

        User user = new User();
        user.setName("een");
        User user2 = new User();
        user2.setName("twee");
        User user3 = new User();
        user3.setName("drie");

        users.add(user);
        users.add(user2);
        users.add(user3);

        Game game = gameService.createNewGame(users);
        int numberOfTerritories = 0;
        for (Territory territory: game.getTerritories()) {
            if(territory.getPlayer() != null) numberOfTerritories++;
        }
        assertTrue("4 players use 40 territories", 42 == numberOfTerritories);
    }

    @Test
    public void dividedFair() {
        List<User> users = new ArrayList<User>();

        User user = new User();
        user.setName("een");
        User user2 = new User();
        user2.setName("twee");
        User user3 = new User();
        user3.setName("drie");
        User user4 = new User();
        user4.setName("vier");

        users.add(user);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        Game game = gameService.createNewGame(users);
        int countUser1 = 0;
        int countUser2 = 0;
        int countUser3 = 0;
        int countUser4 = 0;
        for (Territory territory: game.getTerritories()) {
            if(territory.getPlayer() != null) {
                if(territory.getPlayer().getUser().equals(user)) countUser1++;
                if(territory.getPlayer().getUser().equals(user2)) countUser2++;
                if(territory.getPlayer().getUser().equals(user3)) countUser3++;
                if(territory.getPlayer().getUser().equals(user4)) countUser4++;
            }
        }
        assertTrue("players need to have the same number of territories", countUser1 == countUser2 && countUser1 == countUser3 && countUser1 ==countUser4);
    }

    @Test
    public void randomDividedTerritories() {
        List<User> users = new ArrayList<User>();

        User user = new User();
        user.setName("een");
        User user2 = new User();
        user2.setName("twee");
        User user3 = new User();
        user3.setName("drie");
        User user4 = new User();
        user4.setName("vier");

        users.add(user);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        Game game1 = gameService.createNewGame(users);
        Game game2 = gameService.createNewGame(users);

        Assert.assertFalse(game1.getTerritories().equals(game2.getTerritories()));

    }


}