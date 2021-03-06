package be.kdg.controllers;

import be.kdg.wrappers.UserWrapper;
import be.kdg.exceptions.IllegalUserInviteException;
import be.kdg.exceptions.UnAuthorizedActionException;
import be.kdg.model.Game;
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
 * Creating games, retrieving games and inviting players to a game.
 */

@RestController
public class GameController {
    private static final Logger logger = Logger.getLogger(GameController.class);

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

        logger.info("Game " + game.getId() + " has been created");
        return new ResponseEntity<>(game.getId().toString(), HttpStatus.CREATED);
    }

    @RequestMapping(value = "/acceptGame/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<String> acceptGame(@PathVariable("id") int playerId, @RequestHeader("X-Auth-Token") String token) {
        User user = userServiceImpl.getUser(TokenUtils.getUserNameFromToken(token));
        Player player = playerService.getPlayerById(playerId);
        if (player.getUser().getId() != user.getId()) {
            return new ResponseEntity<>("You may not accept other peoples games", HttpStatus.FORBIDDEN);
        }
        //player.setAccepted(true);
        playerService.acceptGame(playerId);
        logger.info("Player " + playerId + "accepts his game") ;
        playerService.checkIfGameCanStart(playerId);

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
        logger.info("User " + userName + " is invited to game " + gameId);

        return new ResponseEntity<>(newPlayer.getUser().getUsername(), HttpStatus.ACCEPTED);
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
        try {
            newPlayer = gameService.inviteRandomUser(gameId);
        } catch (Exception e) {
            logger.warn(e);
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newPlayer.getUser().getUsername(), HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{username}/games", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<GameWrapper>> getGames(@PathVariable("username") String username, @RequestHeader("X-Auth-Token") String token) {
        List<GameWrapper> gameWrapperList = new ArrayList<>();
        User user = userServiceImpl.getUser(TokenUtils.getUserNameFromToken(token));
        if (!user.getName().equals(username)) {
            return new ResponseEntity<>(gameWrapperList, HttpStatus.FORBIDDEN);
        }
        List<Game> games = gameService.getGames(username);

        for (Game game : games) {
            gameWrapperList.add(new GameWrapper(game));
        }
        return new ResponseEntity<>(gameWrapperList, HttpStatus.OK);
    }

    @RequestMapping(value = "/game/{gameId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
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

    @RequestMapping(value = "/recentlyplayed", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<List<UserWrapper>> getRecentlyPlayedUsers(@RequestHeader("X-Auth-Token") String token) {
        String username = TokenUtils.getUserNameFromToken(token);
       try {
           List<User> users = playerService.getRecentlyPlayed(username);
           List<UserWrapper> userWrappers = new ArrayList<>();
           for(User user : users){
               userWrappers.add(new UserWrapper(user));
           }
           return new ResponseEntity<List<UserWrapper>>(userWrappers, HttpStatus.OK);
       }catch (Exception e ){
           logger.warn(e);
           return new ResponseEntity<List<UserWrapper>>(HttpStatus.BAD_REQUEST);

       }
    }
}
