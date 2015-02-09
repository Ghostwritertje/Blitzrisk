package be.kdg.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;

/**
 * Created by user jorandeboever
 * Date:2/02/15.
 */
@Entity
@Table(name = "t_move")
@Component("move")
public class Move {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "turnId")
    private Turn turn;

    @ManyToOne
    @JoinColumn(name = "destinationTerritoryId")
    private Territory destinationTerritory;

    @ManyToOne
    @JoinColumn(name = "originTerritoryId")
    private Territory originTerritory;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public Territory getDestinationTerritory() {
        return destinationTerritory;
    }

    public void setDestinationTerritory(Territory destinationTerritory) {
        this.destinationTerritory = destinationTerritory;
    }

    public Territory getOriginTerritory() {
        return originTerritory;
    }

    public void setOriginTerritory(Territory originTerritory) {
        this.originTerritory = originTerritory;
    }
}
