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
@Table(name= "t_game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @OneToMany(mappedBy = "game")
    private Set<Player> players = new HashSet<Player>();

    @OneToMany(mappedBy = "game")
    private Set<Turn> turns = new HashSet<Turn>();
}
