package be.kdg.model;


import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Every user plays as a new Player for every game.
 */
@Entity
@Table(name = "t_player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private Integer color;
    //InvitationStatus invitationStatus = InvitationStatus.PENDING;

    @Enumerated(EnumType.STRING)
    InvitationStatus invitationStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gameId")
    private Game game;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Territory> territories = new HashSet<Territory>();

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    private Set<Message> messages = new HashSet<>();

    @OneToMany(mappedBy = "player")
    private List<Turn> turns = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private PlayerStatus playerStatus;

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Set<Territory> getTerritories() {
        return territories;
    }

    public void setTerritories(Set<Territory> territories) {
        this.territories = territories;
    }

    public Integer getId() {
        return Id;
    }

    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }

    public void setInvitationStatus(InvitationStatus invitationStatus) {
        this.invitationStatus = invitationStatus;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    public void setTurns(List<Turn> turns) {
        this.turns = turns;
    }

    public void addTurn(Turn turn) {
        turns.add(turn);
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }
}
