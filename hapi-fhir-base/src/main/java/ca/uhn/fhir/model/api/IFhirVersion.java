package ca.uhn.fhir.model.api;

import ca.uhn.fhir.context.RuntimeResourceDefinition;

public interface IFhirVersion {

	IResource generateProfile(RuntimeResourceDefinition theRuntimeResourceDefinition); 
	
}
