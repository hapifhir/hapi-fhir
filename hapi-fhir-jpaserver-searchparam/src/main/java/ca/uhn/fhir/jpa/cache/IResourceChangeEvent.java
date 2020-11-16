package ca.uhn.fhir.jpa.cache;

import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

public interface IResourceChangeEvent {
	List<IIdType> getCreatedResourceIds();
	List<IIdType> getUpdatedResourceIds();
	List<IIdType> getDeletedResourceIds();
	boolean isEmpty();
}
