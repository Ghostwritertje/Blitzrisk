package be.kdg.dao;

import be.kdg.model.Game;
import be.kdg.model.Player;
import be.kdg.model.User;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple dao for Games
 */

@Service("gameDao")
public class GameDao {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveGame(Game game) {
        try {

            sessionFactory.getCurrentSession().save(game);
        } catch (ConstraintViolationException e) {
            throw e;
        }
    }

    public void updateGame(Game game) {
        try {

            sessionFactory.getCurrentSession().update(game);
        } catch (ConstraintViolationException e) {
            throw e;
        }
    }


    public Game getGame(int gameId) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Game where id = :id");
        query.setParameter("id", gameId);
        return (Game) query.uniqueResult();
    }

    public void removeGame(Game game) {
        sessionFactory.getCurrentSession().delete(game);
    }

    public List<Game> getGamesForUser(User user) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Player player  where player.user = :user ");
        query.setParameter("user", user);
        List<Player> players = query.list();
        List<Game> games = new ArrayList<>();
        for(Player player : players){
            games.add(player.getGame());
        }
        return  games;
    }
}
