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
@Table(name= "t_turn")
public class Turn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    private Integer number;

    @ManyToOne
    @JoinColumn(name = "gameId")
    private Game game;

    @OneToMany(mappedBy = "turn")
    private Set<Move> moves = new HashSet<Move>();

}
