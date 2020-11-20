package ca.uhn.fhir.jpa.cache;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collection;

public interface IResourceChangeListener {
	/**
	 * Called within {@link ResourceChangeListenerRegistryImpl#LOCAL_REFRESH_INTERVAL_MS} of a listener registration
	 *
	 * @param theResourceIds the list of resource ids of resources that match the search parameters the listener was registered with
	 */
	void handleInit(Collection<IIdType> theResourceIds);

	/**
	 * Called by the {@link IResourceChangeListenerRegistry} when resource changes are detected
	 */

	void handleChange(IResourceChangeEvent theResourceChangeEvent);
}
