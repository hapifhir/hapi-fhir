package ca.uhn.fhir.model.api;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

public interface IFhirVersion {

	IResource generateProfile(RuntimeResourceDefinition theRuntimeResourceDefinition);

	Object createServerConformanceProvider(RestfulServer theServer);

	IResourceProvider createServerProfilesProvider(RestfulServer theRestfulServer); 
	
}
