package be.kdg.exceptions;

/**
 * Exception thrown upon trying an illegal turn.
 */
public class IllegalTurnException extends Exception {
    public IllegalTurnException() {
        super("Player is not on turn");
    }
}
