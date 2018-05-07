package org.hl7.fhir.dstu3.hapi.ctx;

import org.hl7.fhir.dstu3.hapi.rest.server.ServerCapabilityStatementProvider;
import org.hl7.fhir.dstu3.hapi.rest.server.ServerProfileProvider;

import ca.uhn.fhir.rest.api.server.IFhirVersionServer;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

public class FhirServerDstu3 implements IFhirVersionServer {
  @Override
  public ServerCapabilityStatementProvider createServerConformanceProvider(RestfulServer theServer) {
    return new ServerCapabilityStatementProvider(theServer);
  }

  @Override
  public IResourceProvider createServerProfilesProvider(RestfulServer theRestfulServer) {
    return new ServerProfileProvider(theRestfulServer);
  }

}
