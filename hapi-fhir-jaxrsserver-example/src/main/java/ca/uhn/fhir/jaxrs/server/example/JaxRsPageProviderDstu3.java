package ca.uhn.fhir.jaxrs.server.example;

import javax.ejb.Stateless;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsPageProvider;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IPagingProvider;

@Path("/")
@Stateless
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
public class JaxRsPageProviderDstu3 extends AbstractJaxRsPageProvider {

    public JaxRsPageProviderDstu3() {
        super(FhirContext.forDstu3());
    }
    
	@Override
	public IPagingProvider getPagingProvider() {
		return JaxRsPatientRestProviderDstu3.PAGE_PROVIDER;
	}

}
