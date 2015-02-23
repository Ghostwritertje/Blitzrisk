package be.kdg;

import be.kdg.exceptions.IllegalMoveException;
import be.kdg.model.*;
import be.kdg.services.GameService;
import be.kdg.services.TurnService;
import be.kdg.services.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.mockito.Mockito.when;

/**
 * Created by Marlies on 22/02/2015.
 */

@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/dispatcher.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class TurnServiceTest {
    private Game game;
    private List<User> users;
    private User user;

    @Autowired
    private UserService userService;
    @Autowired
    private GameService gameService;
    @Autowired
    private TurnService turnService;

    @Before
    public void createGame() {
        user =  Mockito.mock(User.class);
        when(user.getEmail()).thenReturn("user1@test.be");
        when(user.getName()).thenReturn("user1");
        when(user.getName()).thenReturn("user1");

    }

    @Test
    public void attackFromForeignCountry() {

    }
}
