package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.model.primitive.IdDt;

import java.util.Collection;

/**
 * Called by the {@link IVersionChangeListenerRegistry} when a resource change has been detected
 */
public interface IVersionChangeListener {
	void handleCreate(IdDt theResourceId);
	void handleUpdate(IdDt theResourceId);
	void handleDelete(IdDt theResourceId);
	void handleInit(Collection<IdDt> theResourceIds);
}
