package ca.uhn.fhir.jaxrs.server.test;

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
@Produces({MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML})
public class TestJaxRsConformanceRestProvider extends AbstractJaxRsConformanceProvider {

	public TestJaxRsConformanceRestProvider() {
		super("description", "name", "version");
	}

	@Override
	protected ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> getProviders() {
		ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> map = new ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider>();
		map.put(TestJaxRsMockPatientRestProvider.class, new TestJaxRsMockPatientRestProvider());
		map.put(TestJaxRsConformanceRestProvider.class, new TestJaxRsConformanceRestProvider());
		return map;
	}
}
