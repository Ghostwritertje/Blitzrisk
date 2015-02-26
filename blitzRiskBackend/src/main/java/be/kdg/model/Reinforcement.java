package be.kdg.model;

import javax.persistence.*;

/**
 * Created by Marlies on 26/02/2015.
 */
@Entity
@Table(name="t_reinforcement")
public class Reinforcement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @OneToOne
    @JoinColumn(name = "territoryId")
    private Territory territory;

    private Integer numberOfUnits;

    @ManyToOne
    @JoinColumn(name="turnId")
    private Turn turn;

    public Territory getTerritory() {
        return territory;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    public Integer getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(Integer numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public Turn getTurn() {
        return turn;
    }

    public void setTurn(Turn turn) {
        this.turn = turn;
    }
}
