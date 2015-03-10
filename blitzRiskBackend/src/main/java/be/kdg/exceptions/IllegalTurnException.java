package be.kdg.exceptions;

/**
 * Created by Marlies on 9/03/2015.
 */
public class IllegalTurnException extends Exception {
    public IllegalTurnException() {
        super("Player is not on turn");
    }
}
