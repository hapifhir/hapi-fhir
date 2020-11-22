package ca.uhn.fhir.jpa.cache.config;

import ca.uhn.fhir.jpa.cache.IResourceChangeListenerCacheRefresher;
import ca.uhn.fhir.jpa.cache.IResourceChangeListenerRegistry;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCacheRefresherImpl;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerRegistryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(RegisteredResourceListenerFactoryConfig.class)
public class ResourceChangeListenerRegistryConfig {
	@Bean
	IResourceChangeListenerRegistry resourceChangeListenerRegistry() {
		return new ResourceChangeListenerRegistryImpl();
	}

	@Bean
	ResourceChangeListenerCache resourceChangeListenerCache() {
		return new ResourceChangeListenerCache();
	}

	@Bean
	IResourceChangeListenerCacheRefresher resourceChangeListenerCacheRefresher() {
		return new ResourceChangeListenerCacheRefresherImpl();
	}
}
