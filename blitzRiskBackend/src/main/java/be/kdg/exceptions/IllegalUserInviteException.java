package be.kdg.exceptions;

/**
 * Exception thrown on error with inviting a user
 */
public class IllegalUserInviteException extends Exception {

    public IllegalUserInviteException(String message) {
        super(message);
    }
}
