package be.kdg.controllers;

import be.kdg.model.Message;
import be.kdg.model.OutputMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
    @RequestMapping(method= RequestMethod.GET) //for the HTML Angular page that contains our application
    public String viewApplication(){
        return "chat";
    }

    @MessageMapping("/chat") //for websocket traffic
    @SendTo("/topic/message")
    public OutputMessage sendMessage(Message message){ //broadcast a message to /topic/message
                                                       //when a message enters the messagebroker /app/chat
        return new OutputMessage(message, new Date());
    }

}
