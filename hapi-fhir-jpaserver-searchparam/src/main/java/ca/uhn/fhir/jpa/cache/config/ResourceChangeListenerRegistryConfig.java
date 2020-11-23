package ca.uhn.fhir.jpa.cache.config;

import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCacheRefresher;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.cache.RegisteredResourceChangeListener;
import ca.uhn.fhir.jpa.cache.RegisteredResourceListenerFactory;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCacheRefresherImpl;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerRegistryImpl;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ResourceChangeListenerRegistryConfig {
	@Bean
	IResourceChangeListenerRegistry resourceChangeListenerRegistry() {
		return new ResourceChangeListenerRegistryImpl();
	}

	@Bean
	IResourceChangeListenerCacheRefresher resourceChangeListenerCacheRefresher() {
		return new ResourceChangeListenerCacheRefresherImpl();
	}

	@Bean
	RegisteredResourceListenerFactory registeredResourceListenerFactory() {
		return new RegisteredResourceListenerFactory();
	}
	@Bean
	@Scope("prototype")
	RegisteredResourceChangeListener registeredResourceChangeListener(String theResourceName, IResourceChangeListener theResourceChangeListener, SearchParameterMap theSearchParameterMap, long theRemoteRefreshIntervalMs) {
		return new RegisteredResourceChangeListener(theResourceName, theResourceChangeListener, theSearchParameterMap, theRemoteRefreshIntervalMs);
	}
}
