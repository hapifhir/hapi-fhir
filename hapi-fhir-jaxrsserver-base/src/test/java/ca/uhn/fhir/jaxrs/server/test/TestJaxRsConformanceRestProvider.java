package ca.uhn.fhir.jaxrs.server.test;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsConformanceProvider;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.ejb.Stateless;
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
