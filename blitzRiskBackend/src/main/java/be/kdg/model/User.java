package be.kdg.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by user jorandeboever
 * Date:2/02/15.
 */
@Entity
@Table(name= "t_user")
@Component("user")
public class User {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(unique = true)
    private String name;
    private String password;
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "user")
    private Set<Player> players = new HashSet<Player>();

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
