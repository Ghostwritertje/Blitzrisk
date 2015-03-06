package be.kdg.controllers;

//import be.kdg.beans.UserBean;
import be.kdg.beans.UserBean;
import be.kdg.model.User;
import be.kdg.security.TokenUtils;
import be.kdg.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marlies on 5/02/2015.
 */

@RestController
public class UserInfoController {
    private static final Logger logger = Logger.getLogger(UserInfoController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/{username}", method = RequestMethod.PUT)
    public void register(@PathVariable("username") String username, @RequestHeader("email") String email, @RequestHeader("password") String password) {
        logger.info(username + " is registering");
        userService.addUser(username, password, email);

    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User getUser(@RequestHeader("X-Auth-Token") String token) {
        String username = TokenUtils.getUserNameFromToken(token);
        return userService.getUser(username);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = "text/plain")
    @ResponseBody
    public ResponseEntity<String> getToken(@RequestHeader String name, @RequestHeader String password) {
        logger.info(name + " logged in.");

        User verifiedUser = userService.checkLogin(name, password);
        if (verifiedUser != null) {
            return new ResponseEntity<>(TokenUtils.createToken(verifiedUser), HttpStatus.CREATED);
        } else {
            verifiedUser = userService.checkLoginByEmail(name, password);
            if (verifiedUser != null) {
                return new ResponseEntity<>(TokenUtils.createToken(verifiedUser), HttpStatus.CREATED);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public void updateUser(@RequestBody UserBean updatedUser, @RequestHeader("X-Auth-Token") String token){
        String username = TokenUtils.getUserNameFromToken(token);
        User originalUser = userService.getUser(username);

        if(updatedUser.getEmail() != null && !updatedUser.getEmail().equals(originalUser.getEmail())) userService.changeEmail(originalUser.getName(), updatedUser.getEmail());

        if(updatedUser.getPassword() != null && updatedUser.getPassword().length() > 0) userService.changePassword(originalUser.getName(), updatedUser.getPassword());

        if(updatedUser.getEmail() != null && !updatedUser.getName().equals(originalUser.getUsername())) userService.changeUsername(originalUser.getName(), updatedUser.getName());
    }


   /* @RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<User> getUsers() {
        return this.userService.findall();
    }
*/
    @RequestMapping(value = "/secured/users", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<User> getSecuredUsers() {
        return this.userService.findall();
    }

    @RequestMapping(value = "/{username}/friends", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<UserBean> getFriends(@PathVariable("username") String username) {
        List<UserBean> friends = new ArrayList<>();
        for(User user : userService.getFriends(username)){
            friends.add(new UserBean(user));
        }

        return friends;
    }

    @RequestMapping(value = "/{username}/friendrequests", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<UserBean> getFriendRequests(@PathVariable("username") String username) {
        List<UserBean> friends = new ArrayList<>();
        for(User user : userService.getFriendRequests(username)){
            friends.add(new UserBean(user));
        }

        return friends;
    }

    @RequestMapping(value = "/addFriend/{username}", method = RequestMethod.PUT)
    @ResponseBody
    public void addFriend(@PathVariable("username") String username, @RequestHeader("X-Auth-Token") String token) {
        User requestingUser = userService.getUser(TokenUtils.getUserNameFromToken(token));
        logger.info("User " + TokenUtils.getUserNameFromToken(token) + " is adding " + username + " as a friend.");
        userService.addFriend(requestingUser, username);
     //   userService.addFriend(TokenUtils.getUserNameFromToken(token), username);
    }

}