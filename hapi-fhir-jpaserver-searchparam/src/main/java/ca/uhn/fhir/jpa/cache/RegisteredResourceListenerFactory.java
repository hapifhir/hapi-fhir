package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class RegisteredResourceListenerFactory {
	@Autowired
	ApplicationContext myApplicationContext;

	public ResourceChangeListenerCache create(String theResourceName, SearchParameterMap theMap, IResourceChangeListener theResourceChangeListener, long theRemoteRefreshIntervalMs) {
		return myApplicationContext.getBean(ResourceChangeListenerCache.class, theResourceName, theResourceChangeListener, theMap, theRemoteRefreshIntervalMs);
	}
}
