package be.kdg.model;

import javax.persistence.*;

/**
 * Created by user jorandeboever
 * Date:2/02/15.
 */
@Entity
@Table(name = "t_move")
public class Move {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne
    @JoinColumn(name = "turnId")
    private Turn turn;

    @ManyToOne
    @JoinColumn(name = "destinationTerritoryId")
    private Territory destinationTerritory;

    @ManyToOne
    @JoinColumn(name = "originTerritoryId")
    private Territory originTerritory;

    private Integer numberOfUnitsToAttack;

    private Integer originTerritoryStartingNrUnits;
    private Integer originTerritoryRemainingNrUnits;
    private Integer destinationTerritoryStartingNrUnits;
    private Integer destinationTerritoryRemainingNrUnits;

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

    public Integer getNumberOfUnitsToAttack() {
        return numberOfUnitsToAttack;
    }

    public void setNumberOfUnitsToAttack(Integer numberOfUnitsToAttack) {
        this.numberOfUnitsToAttack = numberOfUnitsToAttack;
    }

    public Integer getOriginTerritoryStartingNrUnits() {
        return originTerritoryStartingNrUnits;
    }

    public void setOriginTerritoryStartingNrUnits(Integer originTerritoryStartingNrUnits) {
        this.originTerritoryStartingNrUnits = originTerritoryStartingNrUnits;
    }

    public Integer getOriginTerritoryRemainingNrUnits() {
        return originTerritoryRemainingNrUnits;
    }

    public void setOriginTerritoryRemainingNrUnits(Integer originTerritoryRemainingNrUnits) {
        this.originTerritoryRemainingNrUnits = originTerritoryRemainingNrUnits;
    }

    public Integer getDestinationTerritoryStartingNrUnits() {
        return destinationTerritoryStartingNrUnits;
    }

    public void setDestinationTerritoryStartingNrUnits(Integer destinationTerritoryStartingNrUnits) {
        this.destinationTerritoryStartingNrUnits = destinationTerritoryStartingNrUnits;
    }

    public Integer getDestinationTerritoryRemainingNrUnits() {
        return destinationTerritoryRemainingNrUnits;
    }

    public void setDestinationTerritoryRemainingNrUnits(Integer destinationTerritoryRemainingNrUnits) {
        this.destinationTerritoryRemainingNrUnits = destinationTerritoryRemainingNrUnits;
    }


}
