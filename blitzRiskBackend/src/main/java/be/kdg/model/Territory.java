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
@Table(name = "t_territory")
@Component("territory")
public class Territory {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    private Integer numberOfUnits;

    @ManyToOne
    @JoinColumn(name = "playerId")
    private Player player;

    @OneToMany
    @JoinColumn(name="territoryId")
    private Set<Territory> neighbourTerritories = new HashSet<Territory>();
}
