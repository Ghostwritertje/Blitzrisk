package be.kdg.wrappers;

import be.kdg.model.Message;

import java.util.Date;

/**
 * Created by user jorandeboever
 * Date:2/03/15.
 */
public class MessageWrapper {

    private String message;
    private int id;
    private PlayerWrapper player;

    private Date time;

    public MessageWrapper() {

    }

    public MessageWrapper(Message message) {
        this.message = message.getMessage();
        this.time = message.getTime();
        this.player = new PlayerWrapper(message.getPlayer());
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public PlayerWrapper getPlayer() {
        return player;
    }

    public Date getTime() {
        return time;
    }
}
