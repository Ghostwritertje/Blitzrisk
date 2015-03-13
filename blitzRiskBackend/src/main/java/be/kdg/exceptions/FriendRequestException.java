package be.kdg.exceptions;

/**
 * Exception that can be thrown when a Friend request can not be saved.
 */
public class FriendRequestException extends Exception {
    public FriendRequestException(String message) {
        super(message);
    }
}
