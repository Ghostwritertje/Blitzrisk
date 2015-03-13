package be.kdg.exceptions;

/**
 * Exception thrown upon trying an illegal move.
 */
public class IllegalMoveException extends Exception {

    public IllegalMoveException(String message) {
        super(message);
    }
}
