package be.kdg.model;

import javax.persistence.*;

/**
 * Friend request between 2 users.
 */
@Entity
@Table(name = "t_friendrequest", uniqueConstraints = @UniqueConstraint(columnNames={"userId", "friendId"}))

public class FriendRequest {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private boolean accepted = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
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
