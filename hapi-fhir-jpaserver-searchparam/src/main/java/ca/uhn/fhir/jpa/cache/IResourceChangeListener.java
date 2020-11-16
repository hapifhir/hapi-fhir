package ca.uhn.fhir.jpa.cache;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;

/**
 * Called by the {@link IResourceChangeListenerRegistry} when resource changes have been detected
 */
public interface IResourceChangeListener {
	/**
	 * Called by the {@link IResourceChangeListenerRegistry} when resource changes have been detected
	 */

	void handleChange(ResourceChangeEvent theResourceChangeEvent);
	/**
	 * Called within {@link ResourceChangeListenerRegistryImpl#LOCAL_REFRESH_INTERVAL_MS} of a listener registration
	 *
	 * @param theResourceIds the list of resource ids of resources that match the search parameters the listener was registered with
	 */
	void handleInit(Collection<IIdType> theResourceIds);
}
