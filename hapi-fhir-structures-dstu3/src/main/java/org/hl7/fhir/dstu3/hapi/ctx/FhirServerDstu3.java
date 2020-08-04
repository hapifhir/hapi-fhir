package org.hl7.fhir.dstu3.hapi.ctx;

import ca.uhn.fhir.rest.api.server.IFhirVersionServer;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.hl7.fhir.dstu3.hapi.rest.server.ServerCapabilityStatementProvider;

public class FhirServerDstu3 implements IFhirVersionServer {
  @Override
  public ServerCapabilityStatementProvider createServerConformanceProvider(RestfulServer theServer) {
    return new ServerCapabilityStatementProvider(theServer);
  }

}
