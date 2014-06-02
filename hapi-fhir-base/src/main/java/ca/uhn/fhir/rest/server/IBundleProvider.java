package ca.uhn.fhir.rest.server;

import java.util.List;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.InstantDt;

public interface IBundleProvider {

	List<IResource> getResources(int theFromIndex, int theToIndex);
	
	int size();
	
	InstantDt getPublished();
	
}
