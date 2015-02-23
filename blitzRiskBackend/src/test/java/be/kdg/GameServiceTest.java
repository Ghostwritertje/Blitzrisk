package be.kdg;



import be.kdg.model.Game;
import be.kdg.model.Territory;
import be.kdg.model.User;
import be.kdg.services.GameService;
import be.kdg.services.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by Alexander on 10/2/2015.
 */

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class GameServiceTest {

    private List<User> users;

    @Autowired
    private UserService userManagerService;

    @Autowired(required=true)
    private GameService gameService;

    @Before
    public void createUsers() {
        userManagerService.addUser("Lyle Collins", "lyle", "lyle@collins.be");
        userManagerService.addUser("Margaret Crawford", "margaret", "margaret@crawford.be");
        userManagerService.addUser("Lorenzo Jones", "lorenzo", "lorenzo@jones.be");
        userManagerService.addUser("Guadalupe Howard", "guadalupe", "gaudalupe@howard.be");
        users = new ArrayList<>();
        users.add(userManagerService.getUser("Lyle Collins"));
        users.add(userManagerService.getUser("Margaret Crawford"));
        users.add(userManagerService.getUser("Lorenzo Jones"));
        users.add(userManagerService.getUser("Guadalupe Howard"));
    }

    @After
    public void removeUsers() {
        for(User user: users) {
            //userManagerService.removeUser(user.getName());
        }
    }

    @Test
    public void dividableBy4Players() {
        Game game = gameService.createNewGame();
        gameService.addUsersToGame(users, game);
        int numberOfTerritories = 0;
        for (Territory territory: game.getTerritories()) {
            if(territory.getPlayer() != null) numberOfTerritories++;
        }
        assertTrue("4 players use 40 territories", 40 == numberOfTerritories);
        gameService.removeGame(game);
    }
/*
    @Test
    public void dividableBy3Players() {
        List <User> threeUsers = new ArrayList<>();
        for (int i = 0; i< 3; i++) {
            threeUsers.add(users.get(i));
        }

        Game game = gameService.createNewGame();
        gameService.addUsersToGame(threeUsers, game);
        int numberOfTerritories = 0;
        for (Territory territory: game.getTerritories()) {
            if(territory.getPlayer() != null) numberOfTerritories++;
        }
        assertTrue("3 players use 42 territories", 42 == numberOfTerritories);
        gameService.removeGame(game);
    }

    @Test
    public void dividedFair() {
        Game game = gameService.createNewGame();
        gameService.addUsersToGame(users, game);

        int countUser1 = 0;
        int countUser2 = 0;
        int countUser3 = 0;
        int countUser4 = 0;
        for (Territory territory: game.getTerritories()) {
            if(territory.getPlayer() != null) {
                if(territory.getPlayer().getUser().equals(users.get(0))) countUser1++;
                if(territory.getPlayer().getUser().equals(users.get(1))) countUser2++;
                if(territory.getPlayer().getUser().equals(users.get(2))) countUser3++;
                if(territory.getPlayer().getUser().equals(users.get(3))) countUser4++;
            }
        }
        assertTrue("players need to have the same number of territories", countUser1 == countUser2 && countUser1 == countUser3 && countUser1 ==countUser4);
        gameService.removeGame(game);
    }

    @Test
    public void randomDividedTerritories() {
        Game game1 = gameService.createNewGame();
        Game game2 = gameService.createNewGame();

        gameService.addUsersToGame(users, game1);
        gameService.addUsersToGame(users, game2);

        Assert.assertFalse(game1.getTerritories().equals(game2.getTerritories()));
        gameService.removeGame(game1);
        gameService.removeGame(game2);

    }

    @Test
    public void saveGame() {
        Game game = gameService.createNewGame();
        gameService.addUsersToGame(users, game);
        gameService.saveGame(game);

        Game savedGame=gameService.getGame(game.getId());
        assertTrue(savedGame.getId() == game.getId());
        gameService.removeGame(game);
    }

*/
}