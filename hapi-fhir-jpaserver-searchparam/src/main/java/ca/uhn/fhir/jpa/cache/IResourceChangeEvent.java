package ca.uhn.fhir.jpa.cache;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

/**
 * Registered IResourceChangeListener instances are called with this event to provide them with a list of ids of resources
 * that match the search parameters and that changed from the last time they were checked.
 */
public interface IResourceChangeEvent {
	List<IIdType> getCreatedResourceIds();
	List<IIdType> getUpdatedResourceIds();
	List<IIdType> getDeletedResourceIds();
	boolean isEmpty();
}
