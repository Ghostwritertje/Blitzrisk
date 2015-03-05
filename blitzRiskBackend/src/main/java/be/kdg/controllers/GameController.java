package be.kdg.controllers;

import be.kdg.beans.PlayerBean;
import be.kdg.exceptions.IllegalUserInviteException;
import be.kdg.exceptions.UnAuthorizedActionException;
import be.kdg.model.Game;
import be.kdg.model.InvitationStatus;
import be.kdg.model.Player;
import be.kdg.model.User;
import be.kdg.security.TokenUtils;
import be.kdg.services.*;

import be.kdg.wrappers.GameWrapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> createGame(@RequestHeader("X-Auth-Token") String token) {
        User user = userServiceImpl.getUser(TokenUtils.getUserNameFromToken(token));
        Game game = gameService.createNewGame();
        try {
            gameService.addUserToGame(user, game);
        } catch (IllegalUserInviteException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(game.getId().toString(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/acceptGame/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> acceptGame(@PathVariable("id") int playerId, @RequestHeader("X-Auth-Token") String token) {
        User user = userServiceImpl.getUser(TokenUtils.getUserNameFromToken(token));
        Player player = playerService.getPlayerById(playerId);
        if(player.getUser().getId() != user.getId()) {
            return new ResponseEntity<String>("You may not accept other peoples games", HttpStatus.FORBIDDEN);
        }
        //player.setAccepted(true);
        playerService.acceptGame(playerId);
        return null;
    }

    @RequestMapping(value = "/game/{gameId}/invite/{userName}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> inviteUser(@PathVariable("userName") String userName, @PathVariable("gameId") int gameId, @RequestHeader("X-Auth-Token") String token) {
        User user = userServiceImpl.getUser(TokenUtils.getUserNameFromToken(token));
        try {
            gameService.checkUserInGame(gameId, user);
        } catch (UnAuthorizedActionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }

        Player newPlayer = null;
        try {
            newPlayer = gameService.inviteUser(userName, gameId);
        } catch (IllegalUserInviteException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newPlayer.getUser().getUsername() , HttpStatus.ACCEPTED);
    }

    @RequestMapping(value = "/game/{gameId}/invite-random", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<String> inviteRandomUser(@PathVariable("gameId") int gameId, @RequestHeader("X-Auth-Token") String token) {
        User user = userServiceImpl.getUser(TokenUtils.getUserNameFromToken(token));
        try {
            gameService.checkUserInGame(gameId, user);
        } catch (UnAuthorizedActionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }

        Player newPlayer = null;
        newPlayer = gameService.inviteRandomUser(gameId);
        return new ResponseEntity<>(newPlayer.getUser().getUsername(), HttpStatus.OK);
    }

  /* @RequestMapping(value = "/user/{username}/players", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<PlayerBean> getPlayers(@PathVariable("username") String username, @RequestHeader("X-Auth-Token") String token) {
        User user = userServiceImpl.getUser(TokenUtils.getUserNameFromToken(token));
        if
        List<Player> players = gameService.getPlayers(username);
        List<PlayerBean> playerBeanList = new ArrayList<>();

        for (Player player : players) {
            playerBeanList.add(new PlayerBean(player));
        }

        return playerBeanList;
    }
*/
    @RequestMapping(value = "/user/{username}/games", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<GameWrapper>> getGames(@PathVariable("username") String username, @RequestHeader("X-Auth-Token") String token) {
        List<GameWrapper> gameWrapperList = new ArrayList<>();
        User user = userServiceImpl.getUser(TokenUtils.getUserNameFromToken(token));
        if (!user.getName().equals(username)) {
            return new ResponseEntity<>(gameWrapperList, HttpStatus.FORBIDDEN);
        }
        List<Game> games = gameService.getGames(username);

        for(Game game : games){
            gameWrapperList.add(new GameWrapper(game));
        }
        return new ResponseEntity<>(gameWrapperList, HttpStatus.OK);
    }

    @RequestMapping(value = "/game/{gameId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<GameWrapper> getGame(@PathVariable("gameId") int gameId, @RequestHeader("X-Auth-Token") String token) {
        GameWrapper gameWrapper = null;
        User user = userServiceImpl.getUser(TokenUtils.getUserNameFromToken(token));
        try {
            gameService.checkUserInGame(gameId, user);
        } catch (UnAuthorizedActionException e) {
            return new ResponseEntity<>(gameWrapper, HttpStatus.FORBIDDEN);
        }
        gameWrapper = new GameWrapper(gameService.getGame(gameId));
        return new ResponseEntity<>(gameWrapper, HttpStatus.OK);
    }
}
