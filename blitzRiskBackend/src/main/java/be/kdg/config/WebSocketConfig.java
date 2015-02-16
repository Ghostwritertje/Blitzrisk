package be.kdg.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

/**
 * Created by vman on 7/02/2015.
 */
@Configuration
@EnableWebSocketMessageBroker
@ComponentScan(basePackages = "be.kdg.controller") //websocket traffic will map to controllers
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){ //where communication enters and leaves
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/blitzrisk");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        registry.addEndpoint("/chat").withSockJS();
    }
}
