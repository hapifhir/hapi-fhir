package ca.uhn.fhir.jpa.cache.config;

import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.RegisteredResourceListenerFactory;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCache;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class RegisteredResourceListenerFactoryConfig {
	@Bean
	RegisteredResourceListenerFactory registeredResourceListenerFactory() {
		return new RegisteredResourceListenerFactory();
	}
	@Bean
	@Scope("prototype")
	ResourceChangeListenerCache registeredResourceChangeListener(String theResourceName, IResourceChangeListener theResourceChangeListener, SearchParameterMap theSearchParameterMap, long theRemoteRefreshIntervalMs) {
		return new ResourceChangeListenerCache(theResourceName, theResourceChangeListener, theSearchParameterMap, theRemoteRefreshIntervalMs);
	}
}
