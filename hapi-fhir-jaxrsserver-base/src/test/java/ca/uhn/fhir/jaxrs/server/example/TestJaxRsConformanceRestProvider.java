package ca.uhn.fhir.jaxrs.server.example;

import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.Stateless;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ca.uhn.fhir.jaxrs.server.AbstractJaxRsConformanceProvider;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;

/**
 * Fhir Physician Rest Service
 * 
 * @author axmpm
 *
 */
@Path(TestJaxRsConformanceRestProvider.PATH)
@Stateless
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
public class TestJaxRsConformanceRestProvider extends AbstractJaxRsConformanceProvider {

	public TestJaxRsConformanceRestProvider() {
		super("", "", "");
	}

	@Override
	protected ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> getProviders() {
		ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> map = new ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider>();
		map.put(TestJaxRsMockPatientRestProvider.class, new TestJaxRsMockPatientRestProvider());
		map.put(TestJaxRsConformanceRestProvider.class, new TestJaxRsConformanceRestProvider());
		return map;
	}
}
