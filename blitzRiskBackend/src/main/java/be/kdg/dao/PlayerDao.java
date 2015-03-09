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

import java.util.ArrayList;
import java.util.List;

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

    public void removePlayer(Player player) {
        sessionFactory.getCurrentSession().delete(player);
    }

    public List<User> getRecentlyPlayed(String username) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Player player where player.user.name = :username and player.game.started = true order by player.game.Id desc");
        query.setMaxResults(10);
        query.setParameter("username", username);
        List<Player> usersPlayers = query.list();
        List<Game> games = new ArrayList<>();
        for(Player player : usersPlayers){
            games.add(player.getGame());
        }

        List<User> users = new ArrayList<>();
        List<String> userStrings = new ArrayList<>();

        for(int i = 0; i < games.size() && users.size() < 10; i++){
            List<Player> players = games.get(i).getPlayers();
            for(int j = 0; j< players.size() && users.size() < 10; j++){
                User user = players.get(j).getUser();
                if(!user.getUsername().equals(username) && !userStrings.contains(user.getUsername())){
                    users.add(user);
                    userStrings.add(user.getUsername());
                }
            }
        }
        return users;
    }
}
