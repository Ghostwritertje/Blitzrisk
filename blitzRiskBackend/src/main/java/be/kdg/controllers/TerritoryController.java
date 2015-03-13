package be.kdg.controllers;

import be.kdg.model.Territory;
import be.kdg.services.TerritoryService;
import be.kdg.wrappers.TerritoryNeighbourWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Retrieve the layout for the territories.
 */
@RestController
public class TerritoryController {
    @Autowired
    private TerritoryService territoryService;

    @RequestMapping(value = "/territoryLayout", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<TerritoryNeighbourWrapper> getTerritoryLayout() {
        List<TerritoryNeighbourWrapper> territoryNeighbourWrappers = new ArrayList<TerritoryNeighbourWrapper>();
        for (Iterator<Territory> i = territoryService.getTerritories().iterator(); i.hasNext();){
            territoryNeighbourWrappers.add(new TerritoryNeighbourWrapper(i.next()));
        }
        return territoryNeighbourWrappers;
    }

}
