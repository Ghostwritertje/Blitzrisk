package be.kdg.wrappers;

import be.kdg.model.Game;
import be.kdg.model.Player;
import be.kdg.model.Territory;
import be.kdg.model.Turn;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Alexander on 16/2/2015.
 */
public class GameWrapper {

    private Integer Id;
    private Integer playerTurn;
    private boolean started;
    private List<PlayerWrapper> players = new ArrayList<>();
    private List<Turn> turns = new ArrayList<>();
    private Set<TerritoryWrapper> territories = new HashSet<TerritoryWrapper>();

    public GameWrapper(Game game) {
        Id = game.getId();
        this.playerTurn = game.getPlayerTurn();

        if (game.getPlayers() != null) {
            for (Player player : game.getPlayers()) {
                PlayerWrapper playerWrapper = new PlayerWrapper(player);
                players.add(playerWrapper);
            }
        }
    // not needed yet    this.turns = game.getTurns();

        for (Territory territory : game.getTerritories()) {
            territories.add(new TerritoryWrapper(territory));
        }
        this.started = game.isStarted();
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Integer getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(Integer playerTurn) {
        this.playerTurn = playerTurn;
    }

    @JsonIgnore
    public List<Turn> getTurns() {
        return turns;
    }

    public void setTurns(List<Turn> turns) {
        this.turns = turns;
    }

    public Set<TerritoryWrapper> getTerritories() {
        return territories;
    }

    public void setTerritories(Set<TerritoryWrapper> territories) {
        this.territories = territories;
    }

    public List<PlayerWrapper> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerWrapper> players) {
        this.players = players;
    }
}
