package be.kdg.model;

import org.hibernate.annotations.*;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by user jorandeboever
 * Date:2/02/15.
 */
@Entity
@Table(name= "t_game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private Integer playerTurn;

  //  @Cascade(CascadeType.PERSIST)
    @OneToMany(mappedBy = "game")
    private List<Player> players = new ArrayList<>();

  //  @Cascade(CascadeType.PERSIST)
    @OneToMany(mappedBy = "game")
    private List<Turn> turns = new ArrayList<>();

  //  @Cascade(CascadeType.PERSIST)
    @OneToMany(mappedBy = "game")
    private Set<Territory> territories = new HashSet<Territory>();

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public void setTurns(List<Turn> turns) {
        this.turns = turns;
    }

    public Set<Territory> getTerritories() {
        return territories;
    }

    public void setTerritories(Set<Territory> territories) {
        this.territories = territories;
    }

    public Integer getPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(Integer playerTurn) {
        this.playerTurn = playerTurn;
    }

    public void addTurn(Turn turn) {
        turns.add(turn);
    }

    public Integer getId() {
        return Id;
    }
    public void addPlayer (Player player) {
        players.add(player);
    }
}
