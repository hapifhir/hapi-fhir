package ca.uhn.fhir.jpa.config.dstu3;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2016 University Health Network
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

import ca.uhn.fhir.jpa.subscription.SubscriptionWebsocketHandlerDstu3;

@Configuration
@EnableWebSocket()
public class WebsocketDstu3Config implements WebSocketConfigurer {

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry theRegistry) {
		theRegistry.addHandler(subscriptionWebSocketHandler(), "/websocket/dstu3");
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public WebSocketHandler subscriptionWebSocketHandler() {
		PerConnectionWebSocketHandler retVal = new PerConnectionWebSocketHandler(SubscriptionWebsocketHandlerDstu3.class);
		return retVal;
	}

	@Bean(destroyMethod="destroy")
	public TaskScheduler websocketTaskScheduler() {
		final ThreadPoolTaskScheduler retVal = new ThreadPoolTaskScheduler() {
			private static final long serialVersionUID = 1L;

			@Override
			public void afterPropertiesSet() {
				super.afterPropertiesSet();
				getScheduledThreadPoolExecutor().setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
//				getScheduledThreadPoolExecutor().setRemoveOnCancelPolicy(true);
				getScheduledThreadPoolExecutor().setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
			}
		};
		retVal.setThreadNamePrefix("ws-dstu3-");
		retVal.setPoolSize(5);
		
		return retVal;
	}

}
