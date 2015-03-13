package be.kdg.wrappers;

import be.kdg.model.Territory;

/**
 * Wraps the territory model in a simple wrapper that is used in REST-calls
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

    public Integer getPlayerId() {
        return playerId;
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


}
