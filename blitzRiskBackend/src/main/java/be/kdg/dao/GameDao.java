package be.kdg.dao;

import be.kdg.model.Game;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Marlies on 20/02/2015.
 */

@Service("gameDao")
public class GameDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveGame(Game game) {
        //sessionFactory.getCurrentSession().getTransaction().commit();
        try {

            sessionFactory.getCurrentSession().save(game);
        } catch (ConstraintViolationException e) {
            throw e;
        }
    }

    public Game getGame(int gameId) {
        return (Game) sessionFactory.getCurrentSession().get(Game.class, gameId);
    }

    public void removeGame(Game game) {
        sessionFactory.getCurrentSession().delete(game);
    }
}
