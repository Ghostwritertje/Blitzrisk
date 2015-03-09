package be.kdg.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user jorandeboever
 * Date:2/02/15.
 */
@Entity
@Table(name= "t_turn")
public class Turn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private Integer number;

    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;

    @OneToMany(mappedBy = "turn", fetch = FetchType.EAGER)
    private List<Move> moves = new ArrayList<>();

    @OneToMany(mappedBy = "turn", fetch = FetchType.EAGER)
    private List<Move> calculatedMoves = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "playerId")
    private Player player;

    public Integer getId() {
        return Id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<Move> getCalculatedMoves() {
        return calculatedMoves;
    }

    public void setCalculatedMoves(List<Move> calculatedMoves) {
        this.calculatedMoves = calculatedMoves;
    }
}
