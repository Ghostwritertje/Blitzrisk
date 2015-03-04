package be.kdg.controllers;

import be.kdg.model.Message;
import be.kdg.services.MessageService;
import be.kdg.wrappers.MessageWrapper;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Handles the websocket that is used for chatting in game
 */
@Controller
@RequestMapping("/")
public class ChatController {
    private static final Logger logger = Logger.getLogger(ChatController.class);//gets logger "be.kdg.controllers.ChatController"

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private MessageService messageService;

    /*
    * Messages get send to this method
    * This method broadcasts that message to all clients that are subscribe to the channel
     */
    @MessageMapping("/sendmessage/{gameId}") //for websocket traffic
    public void sendNewMessage(@DestinationVariable int gameId, MessageWrapper message) { //broadcast a message to /topic/message
        //when a message enters the messagebroker /app/chat
        logger.info(message.getPlayer().getUsername() + " sends (" + message.getMessage() + ") in game (" + gameId + ")");
        message.setTime(new Date());
        messageService.saveMessage(message.getMessage(), message.getTime(), message.getPlayer().getId());
        template.convertAndSend("/game/channel/" + gameId, message);
    }

    /*
    * When someone subscribes to the chat-channel,
    * this method sends the past messages 1 time to that user.
     */
    @SubscribeMapping("/channel/{gameId}")
    public List<MessageWrapper> chatInit(@DestinationVariable int gameId) {
        logger.info("User has subscribed to chat for game " + gameId);
        List<MessageWrapper> messages = new ArrayList<>();
        for (Message oldMessage : messageService.getMessagesForGame(gameId)) {
            messages.add(new MessageWrapper(oldMessage));
        }

        return messages;

    }

}
