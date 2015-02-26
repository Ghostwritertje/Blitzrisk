package be.kdg.controllers;

import be.kdg.beans.PlayerBean;
import be.kdg.model.Game;
import be.kdg.model.InvitationStatus;
import be.kdg.model.Player;
import be.kdg.model.User;
import be.kdg.security.TokenUtils;
import be.kdg.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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


        return game.getId().toString();
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
    public String inviteUser(@PathVariable("userId") int userId, @PathVariable("gameId") int gameId) {
        Player newPlayer = gameService.inviteUser(userId, gameId);

        return newPlayer.getUser().getUsername();
    }

    @RequestMapping(value = "/game/{gameId}/invite-random", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String inviteRandomUser(@PathVariable("gameId") int gameId) {
        Player newPlayer = gameService.inviteRandomUser(gameId);
        return newPlayer.getUser().getUsername();
    }

    @RequestMapping(value = "/user/{username}/players", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<PlayerBean> inviteRandomUser(@PathVariable("username") String username) {
        List<Player> players = gameService.getPlayers(username);
        List<PlayerBean> playerBeanList = new ArrayList<>();

        for(Player player : players){
            playerBeanList.add( new PlayerBean(player));
        }

        return playerBeanList;
    }

}
