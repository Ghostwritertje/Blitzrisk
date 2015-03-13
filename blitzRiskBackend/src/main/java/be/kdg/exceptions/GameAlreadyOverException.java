package be.kdg.exceptions;

/**
 * Created by Marlies on 12/03/2015.
 */
public class GameAlreadyOverException extends Exception {
    public GameAlreadyOverException() {
        super("Game has already ended");
    }
}
