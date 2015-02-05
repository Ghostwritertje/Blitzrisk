package be.kdg.controllers;

import be.kdg.model.User;
import be.kdg.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marlies on 5/02/2015.
 */

@RestController
@RequestMapping("/rest/users")
public class UserInfoController {
    @Autowired
    private UserService userService;

    @RequestMapping(method= RequestMethod.GET, produces = "application/json")
        public @ResponseBody List<User> users() {
            return this.userService.findall();
        }
}
