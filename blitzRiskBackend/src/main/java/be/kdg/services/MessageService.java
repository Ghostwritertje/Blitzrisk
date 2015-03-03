package be.kdg.services;

import be.kdg.dao.GameDao;
import be.kdg.dao.MessageDao;
import be.kdg.dao.PlayerDao;
import be.kdg.model.Game;
import be.kdg.model.Message;
import be.kdg.model.Player;
import be.kdg.wrappers.MessageWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by user jorandeboever
 * Date:2/03/15.
 */
@Service("messageService")
public class MessageService {
    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private GameDao gameDao;

    @Transactional
    public List<Message> getMessagesForGame(int i) {
        Game game = gameDao.getGame(i);
        List<Message> messages = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            messages.addAll(player.getMessages());

            // messages.addAll(messageDao.getMessagesForPlayer(player));
        }
        return messages;
    }


    @Transactional
    public void saveMessage(String text, Date time, Integer playerId) {

        messageDao.saveMessage(text, time, playerDao.getPlayerById(playerId));
    }
}
