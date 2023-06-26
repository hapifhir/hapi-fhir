package ca.uhn.fhir.jaxrs.server.test;

import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Stateless;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.server.AbstractJaxRsConformanceProvider;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;

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
