package be.kdg.controllers;


import be.kdg.beans.UserBean;
import be.kdg.exceptions.FriendRequestException;
import be.kdg.model.User;
import be.kdg.security.TokenUtils;
import be.kdg.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller that handles url-s about User.
 */

@RestController
public class UserInfoController {
    private static final Logger logger = Logger.getLogger(UserInfoController.class);

    @Autowired
    private UserService userService;

    @Autowired
    MailSender mailSender;

    @RequestMapping(value = "/user/{username}", method = RequestMethod.PUT)
    public void register(@PathVariable("username") String username, @RequestHeader("email") String emailaddress, @RequestHeader("password") String password) {
        logger.info(username + " is registering");
        userService.addUser(username, password, emailaddress);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(emailaddress);
        email.setSubject("Welcome to BlitzRisk!");
        email.setText("Welcome to BlitzRisk, " + username);
        try {
            mailSender.send(email);
            logger.info("Registration mail send to " + username);
        } catch (Exception e) {
            logger.warn("Couldn't send mail to " + username);
        }
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public UserBean getUser(@RequestHeader("X-Auth-Token") String token) {
        String username = TokenUtils.getUserNameFromToken(token);
        return new UserBean(userService.getUser(username));
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
    public void updateUser(@RequestBody UserBean updatedUser, @RequestHeader("X-Auth-Token") String token) {
        String username = TokenUtils.getUserNameFromToken(token);
        User originalUser = userService.getUser(username);

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(originalUser.getEmail()))
            userService.changeEmail(originalUser.getName(), updatedUser.getEmail());

        if (updatedUser.getPassword() != null && updatedUser.getPassword().length() > 0)
            userService.changePassword(originalUser.getName(), updatedUser.getPassword());

        if (updatedUser.getEmail() != null && !updatedUser.getName().equals(originalUser.getUsername()))
            userService.changeUsername(originalUser.getName(), updatedUser.getName());
    }


/*
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<User> getUsers() {
        return this.userService.findall();
    }
*/

 /*   @RequestMapping(value = "/secured/users", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity getSecuredUsers() {
        try {
            return new ResponseEntity<>(this.userService.findall(), HttpStatus.OK);
        } catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }*/

    @RequestMapping(value = "/friends", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<UserBean> getFriends(@RequestHeader("X-Auth-Token") String token) {
        String username = TokenUtils.getUserNameFromToken(token);
        List<UserBean> friends = new ArrayList<>();
        for (User user : userService.getFriends(username)) {
            friends.add(new UserBean(user));
        }

        return friends;
    }

    @RequestMapping(value = "/friendRequests", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<UserBean> getFriendRequests(@RequestHeader("X-Auth-Token") String token) {
        String username = TokenUtils.getUserNameFromToken(token);
        List<UserBean> friends = new ArrayList<>();
        for (User user : userService.getFriendRequests(username)) {
            friends.add(new UserBean(user));
        }

        return friends;
    }

    @RequestMapping(value = "/addFriend/{username:.+}", method = RequestMethod.POST)
    public ResponseEntity addFriend(@PathVariable("username") String username, @RequestHeader("X-Auth-Token") String token) {
        User requestingUser = userService.getUser(TokenUtils.getUserNameFromToken(token));
        logger.info("User " + TokenUtils.getUserNameFromToken(token) + " is adding " + username + " as a friend.");

        boolean isEmail;
        try {
            InternetAddress internetAddress = new InternetAddress(username);
            internetAddress.validate();
            isEmail = true;
        } catch (AddressException e) {
            isEmail = false;
        }


        try {
            if (isEmail) {
                userService.addFriendByEmail(requestingUser, username);
            } else {
                userService.addFriend(requestingUser, username);
            }
            return new ResponseEntity(HttpStatus.OK);
        } catch (FriendRequestException e) {
            logger.warn(e);
        }
        //if friend didn't exist, invite him by email
        if (isEmail) {
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(username);
            email.setSubject("Invite BlitzRisk");
            email.setText("Greetings, warrior!\n" +
                    requestingUser.getUsername() + " has invited you to BlitzRisk!\n\n" +
                    "You can start playing here: http://localhost:8080/BlitzRisk\n\n" +
                    "We expect to see you on the battlefield soon!");
            try {
                mailSender.send(email);
                logger.info("Invite mail send to " + username);

            } catch (Exception exception) {
                logger.warn("Couldn't send mail to " + username);
            }
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/acceptFriend/{username}", method = RequestMethod.POST)
    public ResponseEntity acceptFriendRequest(@PathVariable("username") String username, @RequestHeader("X-Auth-Token") String token) {
        User requestingUser = userService.getUser(TokenUtils.getUserNameFromToken(token));
        logger.info("User " + TokenUtils.getUserNameFromToken(token) + " is accepting " + username + " as a friend.");
        try {
            userService.acceptFriend(requestingUser, username);
            return new ResponseEntity(HttpStatus.OK);
        } catch (FriendRequestException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}