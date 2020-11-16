package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.model.primitive.IdDt;

import java.util.List;

public interface IResourceChangeEvent {
	List<IdDt> getCreatedResourceIds();

	List<IdDt> getUpdatedResourceIds();

	List<IdDt> getDeletedResourceIds();

	boolean isEmpty();
}
