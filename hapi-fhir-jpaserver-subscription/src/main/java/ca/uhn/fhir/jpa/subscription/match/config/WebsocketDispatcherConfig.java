package ca.uhn.fhir.jpa.subscription.match.config;

/*
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.subscription.match.deliver.websocket.SubscriptionWebsocketHandler;
import ca.uhn.fhir.jpa.subscription.match.deliver.websocket.WebsocketConnectionValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

@Configuration
@EnableWebSocket()
@Controller
public class WebsocketDispatcherConfig implements WebSocketConfigurer {

	@Autowired
	ModelConfig myModelConfig;

	@Bean
	public WebsocketConnectionValidator websocketConnectionValidator() {
		return new WebsocketConnectionValidator();
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry theRegistry) {
		theRegistry.addHandler(subscriptionWebSocketHandler(), myModelConfig.getWebsocketContextPath()).setAllowedOrigins("*");
	}

	@Bean
	public WebSocketHandler subscriptionWebSocketHandler() {
		PerConnectionWebSocketHandler retVal = new PerConnectionWebSocketHandler(SubscriptionWebsocketHandler.class);
		return retVal;
	}

}
