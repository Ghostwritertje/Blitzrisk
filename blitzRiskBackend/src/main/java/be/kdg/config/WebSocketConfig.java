package be.kdg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * Configuration for the websocket
 */

@Configuration
@EnableWebSocketMessageBroker
@ComponentScan(basePackages = "be.kdg.controllers") //websocket traffic will map to controllers
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){ //where communication enters and leaves
        config.enableSimpleBroker("/channel", "/game");
      //  config.enableSimpleBroker("/blitzrisk");
        config.setApplicationDestinationPrefixes("/game"); //, "/topic"
        config.setUserDestinationPrefix("/game");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/chat").withSockJS();
    }
}
