package be.kdg.services;

//import be.kdg.dao.MessageDao;
import be.kdg.dao.PlayerDao;
import be.kdg.model.Message;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Servicelayer to save and retrieve chat messages.
 */
/*@Service("messageService")
public class MessageService {
    @Autowired
    private PlayerDao playerDao;

    @Autowired
    private MessageDao messageDao;


    @Transactional
    public List<Message> getMessagesForGame(int i) {
        List<Message> messages = messageDao.getMessagesForGame(i);
        return Lists.reverse(messages);
    }

    @Transactional
    public void saveMessage(String text, Date time, Integer playerId) {

        messageDao.saveMessage(text, time, playerDao.getPlayerById(playerId));
    }
}*/
