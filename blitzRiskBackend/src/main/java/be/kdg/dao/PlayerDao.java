package be.kdg.dao;

import be.kdg.model.Game;
import be.kdg.model.Player;
import be.kdg.model.User;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Alexander on 20/2/2015.
 */

@Service("playerDao")
public class PlayerDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void savePlayer(Player player) {
        sessionFactory.getCurrentSession().save(player);
    }

    public Player getPlayerById(int id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Player where id = :id");
        query.setParameter("id", id);
        return (Player) query.uniqueResult();
    }

    public void updatePlayer(Player player) {
        sessionFactory.getCurrentSession().update(player);
    }

    public List<Player> getPlayersForUser(User user) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Player player where player.user = :user");
        query.setParameter("user", user);
        return  query.list();
    }

    public List<Player> getPlayersForGame(Game game) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Player player where player.game = :game");
        query.setParameter("game", game);
        return  query.list();
    }
}
