package be.kdg.beans;

import be.kdg.model.InvitationStatus;
import be.kdg.model.Player;

/**
 * Created by user jorandeboever
 * Date:25/02/15.
 */
public class PlayerBean {
    private int Id;
    private int color;
    private InvitationStatus invitationStatus;
    private int gameId;

    public PlayerBean() {
    }

    public PlayerBean(Player player) {
        this.Id = player.getId();
        this.color = player.getColor();
        this.invitationStatus = player.getInvitationStatus();
        this.gameId = player.getGame().getId();
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getGameId() {
        return gameId;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }

    public void setInvitationStatus(InvitationStatus invitationStatus) {
        this.invitationStatus = invitationStatus;
    }


}
