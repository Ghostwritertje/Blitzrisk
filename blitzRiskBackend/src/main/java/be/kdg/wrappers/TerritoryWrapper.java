package be.kdg.wrappers;

import be.kdg.model.Game;
import be.kdg.model.Player;
import be.kdg.model.Territory;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alexander on 16/2/2015.
 */
public class TerritoryWrapper {
    private Integer Id;
    private Integer numberOfUnits;
    private Integer key;
    private PlayerWrapper playerWrapper;

    public TerritoryWrapper(Territory territory) {
        Id = territory.getId();
        this.numberOfUnits = territory.getNumberOfUnits();
        this.key = territory.getKey();
        this.playerWrapper = new PlayerWrapper(territory.getPlayer());
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(Integer numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public PlayerWrapper getPlayerWrapper() {
        return playerWrapper;
    }

    public void setPlayerWrapper(PlayerWrapper playerWrapper) {
        this.playerWrapper = playerWrapper;
    }
}
