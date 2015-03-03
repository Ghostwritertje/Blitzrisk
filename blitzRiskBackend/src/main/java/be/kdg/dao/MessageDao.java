package be.kdg.dao;

import be.kdg.model.Game;
import be.kdg.model.Message;
import be.kdg.model.Move;
import be.kdg.model.Player;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by user jorandeboever
 * Date:2/03/15.
 */

@Service("messageDao")
public class MessageDao {
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private PlayerDao playerDao;


    public List<Message> getMessagesForGame(int gameId){
        Query query = sessionFactory.getCurrentSession().createQuery("from Message message where message.player.game.Id = :gameId");
        query.setParameter("gameId", gameId);
        return  query.list();
    }

    public void saveMessage(String text, Date time, Player player) {
        Message message = new Message();
        message.setTime(time);
        message.setMessage(text);
        message.setPlayer(player);
        sessionFactory.getCurrentSession().saveOrUpdate(message);
    }

    public List<Message> getMessagesForPlayer(Player player) {
        Query query = sessionFactory.getCurrentSession().createQuery("from Message message where message.player = :player");
        query.setParameter("player", player);
        return query.list();
    }
}
