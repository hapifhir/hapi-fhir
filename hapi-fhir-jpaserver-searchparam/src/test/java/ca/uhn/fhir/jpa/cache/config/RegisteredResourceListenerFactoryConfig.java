package ca.uhn.fhir.jpa.cache.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCache;
import ca.uhn.fhir.jpa.cache.ResourceChangeListenerCacheFactory;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

@Configuration
public class RegisteredResourceListenerFactoryConfig {
    @Bean
    ResourceChangeListenerCacheFactory resourceChangeListenerCacheFactory() {
        return new ResourceChangeListenerCacheFactory();
    }

    @Bean
    @Scope("prototype")
    ResourceChangeListenerCache resourceChangeListenerCache(
            String theResourceName,
            IResourceChangeListener theResourceChangeListener,
            SearchParameterMap theSearchParameterMap,
            long theRemoteRefreshIntervalMs) {
        return new ResourceChangeListenerCache(
                theResourceName,
                theResourceChangeListener,
                theSearchParameterMap,
                theRemoteRefreshIntervalMs);
    }
}
