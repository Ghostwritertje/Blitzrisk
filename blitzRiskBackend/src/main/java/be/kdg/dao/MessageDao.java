package be.kdg.dao;

import be.kdg.model.Message;
import be.kdg.model.Player;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Saves and retrieves ChatMessages in database
 */

@Service("messageDao")
public class MessageDao {
    @Autowired
    private SessionFactory sessionFactory;

    public List<Message> getMessagesForGame(int gameId){
        Query query = sessionFactory.getCurrentSession().createQuery("from Message message where message.player.game.Id = :gameId order by message.time desc ");
        query.setParameter("gameId", gameId);
        query.setMaxResults(30);
        return  query.list();
    }

    public void saveMessage(String text, Date time, Player player) {
        Message message = new Message();
        message.setTime(time);
        message.setMessage(text);
        message.setPlayer(player);
        sessionFactory.getCurrentSession().saveOrUpdate(message);
    }

}
