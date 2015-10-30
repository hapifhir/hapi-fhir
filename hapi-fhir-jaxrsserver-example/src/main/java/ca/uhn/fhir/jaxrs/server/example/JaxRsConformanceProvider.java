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
 * Conformance Rest Service
 * 
 * @author Peter Van Houte
 */
@Path("")
@Stateless
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
public class JaxRsConformanceProvider extends AbstractJaxRsConformanceProvider {
	private static final String SERVER_VERSION = "1.0.0";
	private static final String SERVER_DESCRIPTION = "Jax-Rs Test Example Description";
	private static final String SERVER_NAME = "Jax-Rs Test Example";

	public JaxRsConformanceProvider() {
		super(SERVER_VERSION, SERVER_DESCRIPTION, SERVER_NAME);
	}

	@Override
	protected ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> getProviders() {
		ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> map = new ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider>();
		map.put(JaxRsConformanceProvider.class, new JaxRsConformanceProvider());
		map.put(JaxRsPatientRestProvider.class, new JaxRsPatientRestProvider());
		return map;
	}
}
