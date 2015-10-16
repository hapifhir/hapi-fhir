package ca.uhn.fhir.jpa.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.FhirSearchDao;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.ISearchDao;
import ca.uhn.fhir.jpa.subscription.SubscriptionWebsocketHandler;

@Configuration
@EnableTransactionManagement
@EnableWebSocket()
public class BaseDstu2Config extends BaseConfig implements WebSocketConfigurer {

	@Bean
	@Primary
	public FhirContext defaultFhirContext() {
		return fhirContextDstu2();
	}
	
	@Bean(name = "mySystemDaoDstu2", autowire = Autowire.BY_NAME)
	public IFhirSystemDao<ca.uhn.fhir.model.dstu2.resource.Bundle> systemDaoDstu2() {
		ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu2 retVal = new ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu2();
		return retVal;
	}

	@Bean(name = "mySystemProviderDstu2")
	public ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2 systemProviderDstu2() {
		ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2 retVal = new ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2();
		retVal.setDao(systemDaoDstu2());
		return retVal;
	}

	@Bean(name = "myJpaValidationSupportDstu2", autowire = Autowire.BY_NAME)
	public ca.uhn.fhir.jpa.dao.IJpaValidationSupport jpaValidationSupportDstu2() {
		ca.uhn.fhir.jpa.dao.JpaValidationSupportDstu2 retVal = new ca.uhn.fhir.jpa.dao.JpaValidationSupportDstu2();
		return retVal;
	}

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
	
	@Bean(autowire = Autowire.BY_TYPE)
	public ISearchDao searchDao() {
		FhirSearchDao searchDao = new FhirSearchDao();
		return searchDao;
	}

	// <!--
	// <bean id="mySubscriptionWebsocketHandler" class="org.springframework.web.socket.handler.PerConnectionWebSocketHandler">
	// <constructor-arg value="ca.uhn.fhir.jpa.subscription.SubscriptionWebsocketHandler"/>
	// </bean>
	//
	// <bean id="mySubscriptionSecurityInterceptor" class="ca.uhn.fhir.jpa.util.SubscriptionsRequireManualActivationInterceptor"/>
	// -->

}
