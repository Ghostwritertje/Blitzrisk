package be.kdg.controllers;

import be.kdg.model.User;
import be.kdg.security.TokenUtils;
import be.kdg.dao.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Marlies on 5/02/2015.
 */

@RestController
public class UserInfoController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/api/user/{username}/{password}/{email}", method = RequestMethod.PUT)
    public void register(@PathVariable("username") String username,
                         @PathVariable("password") String password,
                         @PathVariable("email") String email) {
        userService.addUser(username, password, email);

    }

    @RequestMapping(value = "/api/token/{username}/{password}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getToken(@PathVariable("username") String username,
                           @PathVariable("password") String password) {
        User user = userService.checkLogin(username, password);
        return TokenUtils.createToken(user);
    }



    @RequestMapping(value = "/api/users", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<User> getUsers() {
        return this.userService.findall();
    }

    @RequestMapping(value = "/api/secured/users", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<User> getSecuredUsers() {
        return this.userService.findall();
    }


}