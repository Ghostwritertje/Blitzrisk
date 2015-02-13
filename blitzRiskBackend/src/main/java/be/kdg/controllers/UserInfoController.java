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

    @RequestMapping(value = "/user/{username}", method = RequestMethod.PUT)
    public void register(@PathVariable("username") String username, @RequestHeader("email") String email, @RequestHeader("password") String password) {

        userService.addUser(username, password, email);

    }

    @RequestMapping(value = "/login}", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public String getToken(@RequestBody User user) {
        User verifiedUser = userService.checkLogin(user.getName(), user.getPassword());
        return TokenUtils.createToken(verifiedUser);
    }



    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<User> getUsers() {
        return this.userService.findall();
    }

    @RequestMapping(value = "/secured/users", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<User> getSecuredUsers() {
        return this.userService.findall();
    }


}