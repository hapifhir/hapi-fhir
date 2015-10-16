package ca.uhn.fhir.jpa.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

import ca.uhn.fhir.jpa.subscription.SubscriptionWebsocketHandler;

@Configuration
@EnableWebSocket()
public class WebsocketDstu2Config implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry theRegistry) {
		theRegistry.addHandler(subscriptionWebSocketHandler(), "/websocket/dstu2");
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public WebSocketHandler subscriptionWebSocketHandler() {
		return new PerConnectionWebSocketHandler(SubscriptionWebsocketHandler.class);
	}

	@Bean
	public TaskScheduler websocketTaskScheduler() {
		ThreadPoolTaskScheduler retVal = new ThreadPoolTaskScheduler();
		retVal.setPoolSize(5);
		return retVal;
	}

}
