package be.kdg.dao;

import be.kdg.model.Player;
import be.kdg.model.User;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Alexander on 20/2/2015.
 */

@Service("playerDao")
public class PlayerDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void savePlayer(Player player) {
        try {
            sessionFactory.getCurrentSession().save(player);
        } catch (ConstraintViolationException e) {
            throw e;
        }
    }

    public Player getPlayerById (int id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Player where id = :id");
        query.setParameter("id", id);
        return (Player) query.uniqueResult();
    }
}
