package ca.uhn.fhir.jaxrs.server.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsPageProvider;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.IPagingProvider;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@Stateless
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
public class TestJaxRsMockPageProviderDstu3 extends AbstractJaxRsPageProvider {

    public TestJaxRsMockPageProviderDstu3() {
        super(FhirContext.forDstu3());
    }
	@Override
	public IPagingProvider getPagingProvider() {
		return TestJaxRsMockPatientRestProviderDstu3.PAGING_PROVIDER;
	}

}
