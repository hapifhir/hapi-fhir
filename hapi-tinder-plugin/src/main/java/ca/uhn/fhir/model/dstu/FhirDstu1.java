package ca.uhn.fhir.model.dstu;

import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.model.api.IFhirVersion;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

public class FhirDstu1 implements IFhirVersion {

	@Override
	public Object createServerConformanceProvider(RestfulServer theServer) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public IResourceProvider createServerProfilesProvider(RestfulServer theRestfulServer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public IResource generateProfile(RuntimeResourceDefinition theRuntimeResourceDefinition) {
		throw new UnsupportedOperationException();
	}

}
