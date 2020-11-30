package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * This interceptor watches all resource changes on the server and compares them to the {@link IResourceChangeListenerCache}
 * entries.  If the resource matches the resource type and search parameter map of that entry, then the corresponding cache
 * will be expired so it is refreshed and listeners are notified of that change within {@link ResourceChangeListenerCacheRefresherImpl#LOCAL_REFRESH_INTERVAL_MS}.
 */
@Service
public class ResourceChangeListenerRegistryInterceptor {
	@Autowired
	private IInterceptorService myInterceptorBroadcaster;
	@Autowired
	private IResourceChangeListenerRegistry myResourceChangeListenerRegistry;

	@PostConstruct
	public void start() {
		myInterceptorBroadcaster.registerInterceptor(this);
	}

	@PreDestroy
	public void stop() {
		myInterceptorBroadcaster.unregisterInterceptor(this);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_CREATED)
	public void created(IBaseResource theResource) {
		handle(theResource);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_DELETED)
	public void deleted(IBaseResource theResource) {
		handle(theResource);
	}

	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	public void updated(IBaseResource theResource) {
		handle(theResource);
	}

	private void handle(IBaseResource theResource) {
		if (theResource == null) {
			return;
		}
		myResourceChangeListenerRegistry.requestRefreshIfWatching(theResource);
	}
}
