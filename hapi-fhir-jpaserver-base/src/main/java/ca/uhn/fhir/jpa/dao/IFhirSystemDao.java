package ca.uhn.fhir.jpa.dao;

import java.util.List;

import ca.uhn.fhir.model.api.IResource;

public interface IFhirSystemDao {

	List<IResource> transaction(List<IResource> theResources);

}
