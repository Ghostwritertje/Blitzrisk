package be.kdg.controllers;

import be.kdg.model.Message;
import be.kdg.model.Player;
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
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Handles the chat.
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
    @RequestMapping(method = RequestMethod.GET) //for the HTML Angular page that contains our application
    public String viewApplication() {
        return "index";
    }
    @MessageMapping("/chat") //for websocket traffic
    @SendTo("/topic/message")
    public OutputMessage sendMessage( Message message) { //broadcast a message to /topic/message
        //when a message enters the messagebroker /app/chat
        MessageHeaderAccessor accessor = new MessageHeaderAccessor();
        accessor.setHeader("foo", "bar");
        return new OutputMessage(message, new Date());
    }*/

    @MessageMapping("/game/sendmessage/{gameId}") //for websocket traffic
    public void sendNewMessage(@DestinationVariable int gameId, MessageWrapper message) { //broadcast a message to /topic/message
        //when a message enters the messagebroker /app/chat
        logger.info(message.getPlayer().getUsername() + " sends (" + message.getMessage() + ") in game (" + gameId + ")");
        message.setTime(new Date());
        messageService.saveMessage(message.getMessage(), message.getTime(), message.getPlayer().getId());

        logger.info("Method 1: Message saved. Message being send to \"" + "/game/channel/" + gameId);
        template.convertAndSend("/game/channel/" + gameId, message);
    }

    @MessageMapping("/sendmessage/{gameId}") //for websocket traffic
    public void sendNewMessage2(@DestinationVariable int gameId, MessageWrapper message) { //broadcast a message to /topic/message
        //when a message enters the messagebroker /app/chat
        logger.info(message.getPlayer().getUsername() + " sends (" + message.getMessage() + ") in game (" + gameId + ")");
        message.setTime(new Date());
        messageService.saveMessage(message.getMessage(), message.getTime(), message.getPlayer().getId());

        logger.info("Method 2: Message saved. Message being send to \"" + "/game/channel/" + gameId);
        template.convertAndSend("/game/channel/" + gameId, message);
    }

    @SubscribeMapping("/channel/{gameId}")
    public List<MessageWrapper> chatInit(@DestinationVariable int gameId) {
        logger.info("User has subscribed!");

        List<MessageWrapper> messages = new ArrayList<>();

        for (Message oldMessage : messageService.getMessagesForGame(gameId)) {
            messages.add(new MessageWrapper(oldMessage));
        }

        return messages;

        //  return messages;
    }

}
