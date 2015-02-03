package be.kdg.controllers;

import be.kdg.model.User;
import be.kdg.persistence.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;

/**
 * Created by user jorandeboever
 * Date:30/01/15.
 */
@Controller

public class HelloController {
    @RequestMapping(method= RequestMethod.GET, value = "/hello")
    public ModelAndView printWelcome(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        mav.addObject("message", "Ik ben de boodschap");
        return mav;
    }
}
