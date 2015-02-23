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
    private Integer id;
    private Integer color;

    public PlayerWrapper(Player player) {
        this.id = player.getId();
        this.color = player.getColor();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}
