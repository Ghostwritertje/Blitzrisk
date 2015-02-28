package be.kdg.controllers;

import be.kdg.services.TurnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * Created by Marlies on 27/02/2015.
 */

@RestController
public class TurnController {

    @Autowired
    private TurnService turnService;

    @RequestMapping(value = "/numberOfReinforcements", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String numberOfReinforcements(@RequestHeader("X-Auth-Token") String token, @RequestHeader("playerId") String playerId) {
        return "" +  turnService.calculateNumberOfReinforcements(playerId);
    }
}
