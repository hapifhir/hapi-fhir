package ca.uhn.fhir.jaxrs.server.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsConformanceProvider;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import jakarta.ejb.Stateless;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.concurrent.ConcurrentHashMap;

/**
 * A conformance provider exposes the mock patient and this provider
 */
@Path("")
@Stateless
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
public class TestJaxRsConformanceRestProviderDstu3 extends AbstractJaxRsConformanceProvider {

	public TestJaxRsConformanceRestProviderDstu3() {
		super(FhirContext.forDstu3(), "description", "name", "version");
	}

	@Override
	protected ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> getProviders() {
		ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> map = new ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider>();
		map.put(TestJaxRsMockPatientRestProviderDstu3.class, new TestJaxRsMockPatientRestProviderDstu3());
		map.put(TestJaxRsConformanceRestProviderDstu3.class, new TestJaxRsConformanceRestProviderDstu3());
		return map;
	}
}
