package be.kdg.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Chatmessage used to send over websockets from client to server and back.
 */
@Entity
@Table(name = "t_message")
public class Message {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private String message;
    @OrderColumn
    private Date time;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "playerId")
    private Player player;

    public String getMessage(){
        return message;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setMessage(String message){
        this.message=message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
