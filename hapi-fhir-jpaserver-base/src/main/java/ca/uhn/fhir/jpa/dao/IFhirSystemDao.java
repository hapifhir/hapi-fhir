package ca.uhn.fhir.jpa.dao;

import java.util.List;

import ca.uhn.fhir.model.api.IResource;

public interface IFhirSystemDao {

	void transaction(List<IResource> theResources);

}
