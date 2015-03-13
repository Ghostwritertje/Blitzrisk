package be.kdg.model;

import be.kdg.services.TerritoryService;
import org.hibernate.annotations.*;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Territories on the gameboard
 */
@Entity
@Table(name = "t_territory")
public class Territory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private Integer numberOfUnits;
    private Integer gameKey;

    @ManyToOne
    @JoinColumn(name = "playerId")
    private Player player;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="territoryId")
    @ForeignKey(name="FK_neighbourkey")
    private Set<Territory> neighbourTerritories = new HashSet<Territory>();

    @ManyToOne
    @JoinColumn(name= "gameId")
    private Game game;


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
        /*TerritoryService territoryService = new TerritoryService();
        //territoryService.getTerritories();
        for (Territory terr : territoryService.getTerritories()){
            if(terr.getId()==this.Id){
                return terr.getNeighbourTerritories();
            }
        }
        return null;*/
    }

    public void setNeighbourTerritories(Set<Territory> neighbourTerritories) {
        this.neighbourTerritories = neighbourTerritories;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Integer getGameKey() {
        return gameKey;
    }

    public void setGameKey(Integer gameId) {
        this.gameKey = gameId;
    }

    public void addNeighbour(Territory territory) {
        neighbourTerritories.add(territory);
    }

    public Integer getId() {
        return Id;
    }
}
