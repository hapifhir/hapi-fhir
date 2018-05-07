package org.hl7.fhir.r4.hapi.ctx;

import ca.uhn.fhir.rest.api.server.IFhirVersionServer;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.hl7.fhir.r4.hapi.rest.server.ServerCapabilityStatementProvider;
import org.hl7.fhir.r4.hapi.rest.server.ServerProfileProvider;

public class FhirServerR4 implements IFhirVersionServer {
  @Override
  public ServerCapabilityStatementProvider createServerConformanceProvider(RestfulServer theServer) {
    return new ServerCapabilityStatementProvider(theServer);
  }

  @Override
  public IResourceProvider createServerProfilesProvider(RestfulServer theRestfulServer) {
    return new ServerProfileProvider(theRestfulServer);
  }

}
