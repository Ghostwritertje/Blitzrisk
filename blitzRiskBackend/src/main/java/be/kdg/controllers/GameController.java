package be.kdg.controllers;

import be.kdg.model.Game;
import be.kdg.model.Player;
import be.kdg.model.User;
import be.kdg.services.*;
import be.kdg.wrappers.GameWrapper;
import be.kdg.wrappers.PlayerWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Alexander on 16/2/2015.
 */

@RestController
public class GameController {

    @Autowired
    GameService gameService;

    @Autowired
    TurnService turnService;

    @Autowired
    PlayerService playerService;

    @Autowired
    UserService userServiceImpl;

    @RequestMapping(value = "/createGame/{userId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String createGame(@PathVariable("userId") int userId) {
        Game game = gameService.createNewGame();
        gameService.addUserToGame(userServiceImpl.getUserById(userId),game);
        System.out.println();
        String json = new String("{\"gameId\": "+game.getId()+"}");
        return json;
    }

    @RequestMapping(value = "/acceptGame", method = RequestMethod.PUT)
    @ResponseBody
    public void acceptGame(@PathVariable("id") String playerId) {

        //player.setAccepted(true);
    }

    @RequestMapping(value = "/inviteUser", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public PlayerWrapper inviteUser(@RequestBody int userId, int gameId) {
        User user = userServiceImpl.getUserById(userId);
        Game game = gameService.getGame(gameId);
        Player player = new Player();
        player.setGame(game);
        player.setUser(user);
        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        return playerWrapper;
    }









   /* @RequestMapping(value = "/createGame", method = RequestMethod.PUT)
    public void register(@PathVariable("username") String username, @RequestHeader("email") String email, @RequestHeader("password") String password) {

        userServiceImpl.addUser(username, password, email);

    }

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<User> getUsers() {
        return this.userServiceImpl.findall();
    }*/

}
