package ca.uhn.fhir.jpa.cache;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;

/**
 * To be notified of resource changes in the repository, implement this interface and register your instance with
 * {@link IResourceChangeListenerRegistry}.
 */
public interface IResourceChangeListener {
	/**
	 * This method is called within {@link ResourceChangeListenerCacheRefresherImpl#LOCAL_REFRESH_INTERVAL_MS} of a listener registration
	 * @param theResourceIds the ids of all resources that match the search parameters the listener was registered with
	 */
	void handleInit(Collection<IIdType> theResourceIds);

	/**
	 * Called by the {@link IResourceChangeListenerRegistry} when matching resource changes are detected
	 */
	void handleChange(IResourceChangeEvent theResourceChangeEvent);
}
