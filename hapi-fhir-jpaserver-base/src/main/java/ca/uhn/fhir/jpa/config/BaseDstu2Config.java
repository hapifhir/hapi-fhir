package ca.uhn.fhir.jpa.config;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;

import ca.uhn.fhir.jpa.dao.FhirSearchDao;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.dao.ISearchDao;
import ca.uhn.fhir.jpa.subscription.SubscriptionWebsocketHandler;

@Configuration
@EnableTransactionManagement
@EnableWebSocket()
@EnableScheduling
public class BaseDstu2Config extends BaseConfig implements WebSocketConfigurer {

	@Bean(name = "mySystemDaoDstu2", autowire = Autowire.BY_NAME)
	public IFhirSystemDao<ca.uhn.fhir.model.dstu2.resource.Bundle> fhirSystemDaoDstu2() {
		ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu2 retVal = new ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu2();
		return retVal;
	}

	@Bean(name = "mySystemProviderDstu2")
	public ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2 systemDaoDstu2() {
		ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2 retVal = new ca.uhn.fhir.jpa.provider.JpaSystemProviderDstu2();
		retVal.setDao(fhirSystemDaoDstu2());
		return retVal;
	}

	@Bean(name = "myJpaValidationSupportDstu2", autowire = Autowire.BY_NAME)
	public ca.uhn.fhir.jpa.dao.IJpaValidationSupport jpaValidationSupportDstu2() {
		ca.uhn.fhir.jpa.dao.JpaValidationSupportDstu2 retVal = new ca.uhn.fhir.jpa.dao.JpaValidationSupportDstu2();
		return retVal;
	}

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(subscriptionWebSocketHandler(), "/websocket/dstu2").withSockJS();
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public WebSocketHandler subscriptionWebSocketHandler() {
		return new PerConnectionWebSocketHandler(SubscriptionWebsocketHandler.class);
	}

	@Bean(autowire = Autowire.BY_TYPE)
	public ISearchDao searchDao() {
		return new FhirSearchDao();
	}

	// <!--
	// <bean id="mySubscriptionWebsocketHandler" class="org.springframework.web.socket.handler.PerConnectionWebSocketHandler">
	// <constructor-arg value="ca.uhn.fhir.jpa.subscription.SubscriptionWebsocketHandler"/>
	// </bean>
	//
	// <bean id="mySubscriptionSecurityInterceptor" class="ca.uhn.fhir.jpa.util.SubscriptionsRequireManualActivationInterceptor"/>
	// -->

}
