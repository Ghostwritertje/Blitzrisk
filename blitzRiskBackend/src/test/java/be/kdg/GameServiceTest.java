package be.kdg;


import be.kdg.model.Game;
import be.kdg.model.Territory;
import be.kdg.model.User;
import be.kdg.services.GameService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @Autowired(required = true)
    private GameService gameService;

    private
    @Mock
    List<User> usersMock;
    private
    @Mock
    User user;


    @Before
     public void createUsers() {
         MockitoAnnotations.initMocks(this);
         usersMock = new ArrayList<>();
         usersMock.add(user);
         usersMock.add(user);
         usersMock.add(user);
     }

    /*@Test
    public void test() {
        assertTrue(true);

    }*/

    @Test
    public void dividableBy4Players() {
        usersMock.add(user);
        Game game = gameService.createNewGame();
        gameService.addUsersToGame(usersMock, game);
        int numberOfTerritories = 0;
        for (Territory territory: game.getTerritories()) {
            if(territory.getPlayer() != null) numberOfTerritories++;
        }
        assertTrue("4 players use 40 territories", 40 == numberOfTerritories);
        gameService.removeGame(game);
    }

    @Test
    public void dividableBy3Players() {
        Game game = gameService.createNewGame();
        gameService.addUsersToGame(usersMock, game);
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
        gameService.addUsersToGame(usersMock, game);

        int countUser1 = 0;
        int countUser2 = 0;
        int countUser3 = 0;
        for (Territory territory: game.getTerritories()) {
            if(territory.getPlayer() != null) {
                if(territory.getPlayer().getUser().equals(usersMock.get(0))) countUser1++;
                if(territory.getPlayer().getUser().equals(usersMock.get(1))) countUser2++;
                if(territory.getPlayer().getUser().equals(usersMock.get(2))) countUser3++;
            }
        }
        assertTrue("players need to have the same number of territories", countUser1 == countUser2 && countUser1 == countUser3);
        gameService.removeGame(game);
    }

    @Test
    public void randomDividedTerritories() {
        Game game1 = gameService.createNewGame();
        Game game2 = gameService.createNewGame();

        gameService.addUsersToGame(usersMock, game1);
        gameService.addUsersToGame(usersMock, game2);

        Assert.assertFalse("territories of 2 games should be different", game1.getTerritories().equals(game2.getTerritories()));
        gameService.removeGame(game1);
        gameService.removeGame(game2);

    }

    /*@Test
    public void saveGame() {
        List<User> usersMock = new ArrayList<>();
        usersMock.add(user);
        usersMock.add(user);
        usersMock.add(user);
        Game game = gameService.createNewGame();
        gameService.addUsersToGame(usersMock, game);
        gameService.updateGame(game);

        Game savedGame=gameService.getGame(game.getId());
        assertTrue("game should be saved", savedGame.getId() == game.getId());
        gameService.removeGame(game);
    }*/
}