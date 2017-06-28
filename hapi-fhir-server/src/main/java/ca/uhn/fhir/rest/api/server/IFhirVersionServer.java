package ca.uhn.fhir.rest.api.server;

import org.hl7.fhir.instance.model.api.IBaseResource;

import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.IServerConformanceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

/**
 * This class is the server specific equivalent to {@link IFhirVersion}
 */
public interface IFhirVersionServer {

	IServerConformanceProvider<? extends IBaseResource> createServerConformanceProvider(RestfulServer theRestfulServer); 
	
	IResourceProvider createServerProfilesProvider(RestfulServer theRestfulServer);
	
}
