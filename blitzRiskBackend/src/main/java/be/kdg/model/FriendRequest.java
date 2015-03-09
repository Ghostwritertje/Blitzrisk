package be.kdg.model;

import javax.persistence.*;

/**
 * Created by user jorandeboever
 * Date:7/03/15.
 */
@Entity
@Table(name = "t_friendrequest", uniqueConstraints = @UniqueConstraint(columnNames={"userId", "friendId"}))

public class FriendRequest {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private boolean accepted = false;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friendId")
    private User friend;

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }
}
