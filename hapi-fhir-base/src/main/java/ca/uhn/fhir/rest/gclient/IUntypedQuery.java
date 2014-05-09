package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.model.api.IResource;


public interface IUntypedQuery {

	IQuery forResource(String theResourceName);

	IQuery forResource(Class<? extends IResource> theClass);
	
}
