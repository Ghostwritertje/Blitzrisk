package be.kdg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Marlies on 6/01/2015.
 */

@Controller
public class UserController {
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String getLogin() {
        return "login";
    }

    @RequestMapping(value = "/loginSucces", method = RequestMethod.GET)
    public String loggedIn() {
        return "loginSucces";
    }
}
