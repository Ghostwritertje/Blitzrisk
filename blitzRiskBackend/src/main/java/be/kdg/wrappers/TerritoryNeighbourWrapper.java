package be.kdg.wrappers;

import be.kdg.model.Territory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Gunther Claessens.
 */
public class TerritoryNeighbourWrapper {
    private int territoryKey;
    private List<Integer> neighbours = new ArrayList<Integer>();

    public TerritoryNeighbourWrapper(Territory territory){
        this.territoryKey = territory.getKey();
        for (Iterator<Territory> i = territory.getNeighbourTerritories().iterator(); i.hasNext();){
            Territory ter = i.next();
            this.neighbours.add(ter.getKey());
        }
    }

    public int getTerritoryKey() {
        return territoryKey;
    }

    public List<Integer> getNeighbours() {
        return neighbours;
    }

    public void setTerritoryKey(int territoryKey) {
        this.territoryKey = territoryKey;
    }

    public void setNeighbours(List<Integer> neighbours) {
        this.neighbours = neighbours;
    }
}
