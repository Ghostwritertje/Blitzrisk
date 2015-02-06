package be.kdg.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by user jorandeboever
 * Date:2/02/15.
 */
@Entity
@Table(name = "t_territory")
@Component("territory")
public class Territory {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private Integer numberOfUnits;
    private Integer gameId;

    @ManyToOne
    @JoinColumn(name = "playerId")
    private Player player;

    @OneToMany
    @JoinColumn(name="territoryId")
    private Set<Territory> neighbourTerritories = new HashSet<Territory>();

    public Integer getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(Integer numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<Territory> getNeighbourTerritories() {
        return neighbourTerritories;
    }

    public void setNeighbourTerritories(Set<Territory> neighbourTerritories) {
        this.neighbourTerritories = neighbourTerritories;
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(Integer gameId) {
        this.gameId = gameId;
    }

    public void addNeighbour(Territory territory) {
        neighbourTerritories.add(territory);
    }
}
