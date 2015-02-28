package be.kdg.wrappers;

import be.kdg.model.Move;
import be.kdg.model.Territory;

/**
 * Created by Marlies on 28/02/2015.
 */
public class MoveWrapper {
    private int id;
    private int turnId;
    private int origin;
    private int destination;
    private int units;
    private int calculatedUnits;

    public MoveWrapper (Move move) {
        id = move.getId();
        origin = move.getOriginTerritory().getId();
        destination = move.getDestinationTerritory().getId();
        units = move.getNumberOfUnitsToAttack();
        calculatedUnits = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    public int getDestination() {
        return destination;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public int getCalculatedUnits() {
        return calculatedUnits;
    }

    public void setCalculatedUnits(int calculatedUnits) {
        this.calculatedUnits = calculatedUnits;
    }

    public int getTurnId() {
        return turnId;
    }

    public void setTurnId(int turnId) {
        this.turnId = turnId;
    }
}
