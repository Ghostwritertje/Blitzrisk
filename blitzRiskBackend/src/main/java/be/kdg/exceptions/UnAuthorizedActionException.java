package be.kdg.exceptions;

/**
 * Exception thrown upon unauthorized action.
 */
public class UnAuthorizedActionException extends Exception {
    public UnAuthorizedActionException(String message) {
        super(message);
    }
}
