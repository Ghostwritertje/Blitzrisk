package be.kdg.wrappers;

import be.kdg.model.Territory;

/**
 * Created by Marlies on 28/02/2015.
 */
public class UpdatedTerritoriesWrapper {
    private int gameId;
    private int territoryId;
    private int playerId;
    private int units;

    public UpdatedTerritoriesWrapper(Territory territory) {
        gameId = territory.getGame().getId();
        territoryId = territory.getId();
        units = territory.getNumberOfUnits();
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getTerritoryId() {
        return territoryId;
    }

    public void setTerritoryId(int territoryId) {
        this.territoryId = territoryId;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
