package be.kdg.controllers;

import be.kdg.beans.PlayerBean;
import be.kdg.model.Game;
import be.kdg.model.InvitationStatus;
import be.kdg.model.Player;
import be.kdg.model.User;
import be.kdg.security.TokenUtils;
import be.kdg.services.*;

import be.kdg.wrappers.GameWrapper;
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
    public void acceptGame(@PathVariable("id") int playerId) {
        //player.setAccepted(true);
        playerService.acceptGame(playerId);
    }

    @RequestMapping(value = "/game/{gameId}/invite/{userName}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String inviteUser(@PathVariable("userName") String userName, @PathVariable("gameId") int gameId) {
        Player newPlayer = gameService.inviteUser(userName, gameId);
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
    public List<PlayerBean> getPlayers(@PathVariable("username") String username) {
        List<Player> players = gameService.getPlayers(username);
        List<PlayerBean> playerBeanList = new ArrayList<>();

        for (Player player : players) {
            playerBeanList.add(new PlayerBean(player));
        }

        return playerBeanList;
    }

    @RequestMapping(value = "/user/{username}/games", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<GameWrapper> getGames(@PathVariable("username") String username) {
        List<Game> games = gameService.getGames(username);
        List<GameWrapper> gameWrapperList = new ArrayList<>();

        for(Game game : games){
            gameWrapperList.add(new GameWrapper(game));
        }
        return gameWrapperList;
    }

    @RequestMapping(value = "/game/{gameId}", method = RequestMethod.GET, produces = "application/json")
    public GameWrapper getGame(@PathVariable("gameId") int gameId) {
        return new GameWrapper(gameService.getGame(gameId));
    }

}
