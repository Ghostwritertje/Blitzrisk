package be.kdg.controllers;

import be.kdg.model.Game;
import be.kdg.model.User;
import be.kdg.security.TokenUtils;
import be.kdg.services.GameService;
import be.kdg.services.TurnService;
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

    @RequestMapping(value = "/createGame", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public GameWrapper createGame(@RequestBody List<User> users) {
        Game game = gameService.createNewGame(users);
        GameWrapper wrapper = new GameWrapper(game);
        return wrapper;
    }





   /* @RequestMapping(value = "/createGame", method = RequestMethod.PUT)
    public void register(@PathVariable("username") String username, @RequestHeader("email") String email, @RequestHeader("password") String password) {

        userService.addUser(username, password, email);

    }

    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<User> getUsers() {
        return this.userService.findall();
    }*/

}
