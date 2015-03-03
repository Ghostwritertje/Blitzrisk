package be.kdg.controllers;

import be.kdg.model.Message;
import be.kdg.model.Player;
import be.kdg.services.MessageService;
import be.kdg.wrappers.MessageWrapper;
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

    @MessageMapping("/notify/{gameId}") //for websocket traffic
    public void sendNewMessage(@DestinationVariable int gameId, MessageWrapper message) { //broadcast a message to /topic/message
        //when a message enters the messagebroker /app/chat
        message.setTime(new Date());
        messageService.saveMessage(message.getMessage(), message.getTime(), message.getPlayer().getId());

        template.convertAndSend("/topic/push/" + gameId, message);
    }

    @SubscribeMapping("/notify/{gameId}")
    public List<MessageWrapper> chatInit(@DestinationVariable int gameId) {
        List<MessageWrapper> messages = new ArrayList<>();
        Player player = new Player();
        player.setColor(5);
        Message message = new Message();
        message.setMessage("Welcome");
        message.setPlayer(player);
        messages.add( new MessageWrapper(message));
        return messages;

        /*for (Message oldMessage : messageService.getMessagesForGame(gameId)) {
            template.convertAndSend("/topic/push/" + gameId, oldMessage);
        }
*/
      //  return messages;
    }

}
