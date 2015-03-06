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
    private int originNrOfUnits;
    private int originPlayer;
    private int destination;
    private int destinationNrOfUnits;
    private int destinationPlayer;
    private int unitsToAttackOrReinforce;

    public MoveWrapper (Move move) {
        id = move.getId();
        origin = move.getOriginTerritory().getId();
        originNrOfUnits = move.getOriginTerritory().getNumberOfUnits();
        originPlayer = move.getOriginTerritory().getPlayer().getId();
        turnId = move.getTurn().getId();
        destination = move.getDestinationTerritory().getId();
        destinationNrOfUnits = move.getDestinationTerritory().getNumberOfUnits();
        destinationPlayer = move.getDestinationTerritory().getPlayer().getId();
        unitsToAttackOrReinforce = move.getNumberOfUnitsToAttack();
    }

    public MoveWrapper() {
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

    public int getUnitsToAttackOrReinforce() {
        return unitsToAttackOrReinforce;
    }

    public void setUnitsToAttackOrReinforce(int unitsToAttackOrReinforce) {
        this.unitsToAttackOrReinforce = unitsToAttackOrReinforce;
    }

    public int getTurnId() {
        return turnId;
    }

    public void setTurnId(int turnId) {
        this.turnId = turnId;
    }

    public int getOriginNrOfUnits() {
        return originNrOfUnits;
    }

    public void setOriginNrOfUnits(int originNrOfUnits) {
        this.originNrOfUnits = originNrOfUnits;
    }

    public int getOriginPlayer() {
        return originPlayer;
    }

    public void setOriginPlayer(int originPlayer) {
        this.originPlayer = originPlayer;
    }

    public int getDestinationNrOfUnits() {
        return destinationNrOfUnits;
    }

    public void setDestinationNrOfUnits(int destinationNrOfUnits) {
        this.destinationNrOfUnits = destinationNrOfUnits;
    }

    public int getDestinationPlayer() {
        return destinationPlayer;
    }

    public void setDestinationPlayer(int destinationPlayer) {
        this.destinationPlayer = destinationPlayer;
    }
}
