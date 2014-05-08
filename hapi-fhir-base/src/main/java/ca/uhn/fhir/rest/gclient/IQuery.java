package ca.uhn.fhir.rest.gclient;

import ca.uhn.fhir.model.api.IResource;


public interface IQuery {

	IFor forResource(String theResourceName);

	IFor forResource(Class<? extends IResource> theClass);
	
}
