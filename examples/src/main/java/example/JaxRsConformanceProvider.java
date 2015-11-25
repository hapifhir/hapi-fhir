package example;

import java.util.concurrent.ConcurrentHashMap;

import javax.ejb.EJB;
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
 // START SNIPPET: jax-rs-conformance
@Path("")
@Stateless
@Produces({ MediaType.APPLICATION_JSON, Constants.CT_FHIR_JSON, Constants.CT_FHIR_XML })
public class JaxRsConformanceProvider extends AbstractJaxRsConformanceProvider {

  @EJB
  private JaxRsPatientRestProvider provider;

  public JaxRsConformanceProvider() {
    super("My Server Version", "My Server Description", "My Server Name");
  }

  @Override
  protected ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> getProviders() {
    ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider> map = new ConcurrentHashMap<Class<? extends IResourceProvider>, IResourceProvider>();
    map.put(JaxRsConformanceProvider.class, this);
    map.put(JaxRsPatientRestProvider.class, provider);
    return map;
  }
}
// END SNIPPET: jax-rs-conformance
