package be.kdg.controllers;

import be.kdg.model.Game;
import be.kdg.model.InvitationStatus;
import be.kdg.model.Player;
import be.kdg.model.User;
import be.kdg.security.TokenUtils;
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

    @RequestMapping(value = "/createGame", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String createGame(@RequestHeader("X-Auth-Token") String token) {
        User user = userServiceImpl.getUser(TokenUtils.getUserNameFromToken(token));
        Game game = gameService.createNewGame();
        gameService.addUserToGame(user, game);

        String json = new String("{\"gameId\": " + game.getId() + "}");

        return json;
    }

    @RequestMapping(value = "/acceptGame/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public void acceptGame(@PathVariable("id") String playerId) {
        Player player = playerService.getPlayerById(Integer.parseInt(playerId));
        player.setInvitationStatus(InvitationStatus.ACCEPTED);
        playerService.updatePlayer(player);
        //player.setAccepted(true);
    }

    @RequestMapping(value = "/game/{gameId}/invite/{userId}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody

    public PlayerWrapper inviteUser(@PathVariable("userId") int userId, @PathVariable("gameId") int gameId) {
        Player newPlayer = gameService.inviteUser(userId, gameId);

        PlayerWrapper playerWrapper = new PlayerWrapper(newPlayer);
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
