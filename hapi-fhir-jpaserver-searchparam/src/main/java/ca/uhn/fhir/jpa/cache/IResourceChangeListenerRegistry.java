package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import java.util.Iterator;

public interface IResourceChangeListenerRegistry {

	/**
	 * Register a listener in order to be notified whenever a resource matching the provided SearchParameterMap
	 * changes in any way.  If the change happened on the same jvm process where this registry resides, then the listener will be called
	 * within {@link ResourceChangeListenerCacheRefresherImpl#LOCAL_REFRESH_INTERVAL_MS} of the change happening.  If the change happened
	 * on a different jvm process, then the listener will be called within theRemoteRefreshIntervalMs.
	 * @param theResourceName           the name of the resource the listener should be notified about
	 * @param theSearchParameterMap     the listener will only be notified of changes to resources that match this map
	 * @param theResourceChangeListener the listener to be notified
	 * @param theRemoteRefreshIntervalMs the number of milliseconds between checking the database for changed resources that match the search parameter map
	 * @throws ca.uhn.fhir.parser.DataFormatException      if theResourceName is not valid
	 * @throws IllegalArgumentException if theSearchParamMap cannot be evaluated in-memory
	 * @return RegisteredResourceChangeListener that stores the resource id cache, and the next refresh time
	 * @return
	 */
	RegisteredResourceChangeListener registerResourceResourceChangeListener(String theResourceName, SearchParameterMap theSearchParameterMap, IResourceChangeListener theResourceChangeListener, long theRemoteRefreshIntervalMs);

	/**
	 * Unregister a listener from this service
	 *
	 * @param theResourceChangeListener
	 */
	void unregisterResourceResourceChangeListener(IResourceChangeListener theResourceChangeListener);

	@VisibleForTesting
	void clearListenersForUnitTest();

	@Nonnull
	Iterator<RegisteredResourceChangeListener> iterator();
	// FIXME KHS can remove these?

	void requestRefreshIfWatching(IBaseResource theResource);

	boolean contains(RegisteredResourceChangeListener theEntry);
}
