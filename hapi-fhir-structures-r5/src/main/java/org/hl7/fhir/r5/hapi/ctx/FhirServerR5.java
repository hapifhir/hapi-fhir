package org.hl7.fhir.r5.hapi.ctx;

import ca.uhn.fhir.rest.api.server.IFhirVersionServer;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.provider.ServerCapabilityStatementProvider;

public class FhirServerR5 implements IFhirVersionServer {
	@Override
	public ServerCapabilityStatementProvider createServerConformanceProvider(RestfulServer theServer) {
		return new ServerCapabilityStatementProvider(theServer);
	}

}
