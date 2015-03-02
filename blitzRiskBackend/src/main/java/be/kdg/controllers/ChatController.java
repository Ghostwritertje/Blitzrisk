package be.kdg.controllers;

import be.kdg.model.Message;
import be.kdg.model.OutputMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

/**
 * Created by vman on 7/02/2015.
 */
@Controller
@RequestMapping("/") //context root, mapped to ViewApplication(), so that index.jsp is used as the view
public class ChatController {
    @Autowired
    private SimpMessagingTemplate template;


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
    }

    @MessageMapping("/notify/{gameId}") //for websocket traffic
    //  @SendTo("/topic/message")
    public void sendNewMessage(@DestinationVariable int gameId, Message message) { //broadcast a message to /topic/message
        //when a message enters the messagebroker /app/chat
        Message outputMessage = new OutputMessage(message, new Date());

        template.convertAndSend("/topic/push/" + gameId, outputMessage );
    }

}
