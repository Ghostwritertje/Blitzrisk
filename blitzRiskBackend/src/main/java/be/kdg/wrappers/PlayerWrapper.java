package be.kdg.wrappers;

import be.kdg.model.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Alexander on 16/2/2015.
 */
public class PlayerWrapper {
    private Integer Id;
    private Integer color;

    public PlayerWrapper(Player player) {
        Id = player.getId();
        this.color = player.getColor();
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}
