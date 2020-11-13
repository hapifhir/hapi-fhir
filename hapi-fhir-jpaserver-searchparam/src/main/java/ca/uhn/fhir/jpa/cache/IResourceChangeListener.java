package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.model.primitive.IdDt;

import java.util.Collection;

/**
 * Called by the {@link IResourceChangeListenerRegistry} when a resource change has been detected
 */
public interface IResourceChangeListener {
	void handleCreate(IdDt theResourceId);
	void handleUpdate(IdDt theResourceId);
	void handleDelete(IdDt theResourceId);

	/**
	 * Called within {@link ResourceChangeListenerRegistryImpl#LOCAL_REFRESH_INTERVAL_MS} of a listener registration
	 *
	 * @param theResourceIds the list of resource ids of resources that match the search parameters the listener was registered with
	 */
	void handleInit(Collection<IdDt> theResourceIds);
}
