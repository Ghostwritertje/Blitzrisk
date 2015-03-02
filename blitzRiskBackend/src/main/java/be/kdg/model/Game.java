package be.kdg.model;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.*;

/**
 * Created by user jorandeboever
 * Date:2/02/15.
 */
@Entity
@Table(name = "t_game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private boolean started = false;

    private Integer playerTurn;

    //  @Cascade(CascadeType.PERSIST)
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Player> players = new ArrayList<>();

    //  @Cascade(CascadeType.PERSIST)
    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
    private List<Turn> turns = new ArrayList<>();



    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Territory> territories = new ArrayList<>();
    
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

    public List<Territory> getTerritories() {
        return territories;
    }

    public void setTerritories(List<Territory> territories) {

        for (Territory territory: territories){
            territory.setGame(this);
        }
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

    public void addPlayer(Player player) {
        players.add(player);
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void assignRandomTerritories() {
     //   this.createTerritories();

        Collections.shuffle(territories);

        while (territories.size() % players.size() != 0) {
            territories.remove(territories.size() - 1);
        }
        int i = 0;
        for (Territory territory : territories) {
            territory.setPlayer(players.get(i++));
            territory.setNumberOfUnits(1);

            if (i == players.size()) {
                i = 0;
            }
        }

    }
}
