package org.hl7.fhir.dstu2016may.hapi.ctx;

import org.hl7.fhir.dstu2016may.hapi.rest.server.ServerConformanceProvider;

import ca.uhn.fhir.rest.api.server.IFhirVersionServer;
import ca.uhn.fhir.rest.server.RestfulServer;

public class FhirServerDstu2_1 implements IFhirVersionServer {

    @Override
    public ServerConformanceProvider createServerConformanceProvider(RestfulServer theServer) {
        return new ServerConformanceProvider(theServer);
    }
}
