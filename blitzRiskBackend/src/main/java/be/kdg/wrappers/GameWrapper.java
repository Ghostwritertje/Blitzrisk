package be.kdg.wrappers;

import be.kdg.model.Game;
import be.kdg.model.Player;
import be.kdg.model.Territory;
import be.kdg.model.Turn;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    private List<PlayerWrapper> playerWrappers = new ArrayList<>();
    private List<Turn> turns = new ArrayList<>();
    private Set<TerritoryWrapper> territoryWrappers = new HashSet<TerritoryWrapper>();

    public GameWrapper(Game game) {
        Id = game.getId();
        this.playerTurn = game.getPlayerTurn();

        if (game.getPlayers() != null) {
            for (Player player : game.getPlayers()) {
                PlayerWrapper playerWrapper = new PlayerWrapper(player);
                playerWrappers.add(playerWrapper);
            }
        }
        this.turns = game.getTurns();

        for (Territory territory : game.getTerritories()) {
            territoryWrappers.add(new TerritoryWrapper(territory));
        }
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
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

    public Set<TerritoryWrapper> getTerritoryWrappers() {
        return territoryWrappers;
    }

    public void setTerritoryWrappers(Set<TerritoryWrapper> territoryWrappers) {
        this.territoryWrappers = territoryWrappers;
    }

    public List<PlayerWrapper> getPlayerWrappers() {
        return playerWrappers;
    }

    public void setPlayerWrappers(List<PlayerWrapper> playerWrappers) {
        this.playerWrappers = playerWrappers;
    }
}
