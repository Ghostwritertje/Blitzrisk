package be.kdg.wrappers;

import be.kdg.model.Territory;

/**
 * Created by Alexander on 16/2/2015.
 */
public class TerritoryWrapper {
    private Integer Id;
    private Integer numberOfUnits;
    private Integer key;
    private Integer playerId;

    public TerritoryWrapper(Territory territory) {
        Id = territory.getId();
        this.numberOfUnits = territory.getNumberOfUnits();
        this.key = territory.getGameKey();
        if (territory.getPlayer() != null) {
            this.playerId = territory.getPlayer().getId();
        }
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

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }
}
