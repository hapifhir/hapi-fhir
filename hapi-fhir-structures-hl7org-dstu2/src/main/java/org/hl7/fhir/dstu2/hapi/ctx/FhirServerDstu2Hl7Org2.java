package org.hl7.fhir.dstu2.hapi.ctx;

import ca.uhn.fhir.rest.api.server.IFhirVersionServer;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.hl7.fhir.dstu2.hapi.rest.server.ServerConformanceProvider;
import org.hl7.fhir.dstu2.hapi.rest.server.ServerProfileProvider;

public class FhirServerDstu2Hl7Org2 implements IFhirVersionServer {

  @Override
  public ServerConformanceProvider createServerConformanceProvider(RestfulServer theServer) {
    return new ServerConformanceProvider(theServer);
  }

  @Override
  public IResourceProvider createServerProfilesProvider(RestfulServer theRestfulServer) {
    return new ServerProfileProvider(theRestfulServer);
  }

}
