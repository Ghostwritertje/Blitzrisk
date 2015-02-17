package be.kdg.controllers;

import be.kdg.model.User;
import be.kdg.security.TokenUtils;
import be.kdg.services.UserManagerServicee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Marlies on 5/02/2015.
 */

@RestController
public class UserInfoController {
    @Autowired
    private UserManagerServicee userService;

    @RequestMapping(value = "/user/{username}", method = RequestMethod.PUT)
    public void register(@PathVariable("username") String username, @RequestHeader("email") String email, @RequestHeader("password") String password) {

        userService.addUser(username, password, email);

    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = "text/plain")
    @ResponseBody
    public ResponseEntity<String> getToken(@RequestHeader String name, @RequestHeader String password) {

        User verifiedUser = userService.checkLogin(name, password);
        if (verifiedUser != null) {
            return new ResponseEntity<>(TokenUtils.createToken(verifiedUser), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

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