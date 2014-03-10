package example;

import java.util.Arrays;
import java.util.Collection;

import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.ISecurityManager;
import ca.uhn.fhir.rest.server.RestfulServer;

public class RestfulServlet extends RestfulServer {

	private static final long serialVersionUID = 1L;
	
	private Collection<IResourceProvider> myResourceProviders;

	public RestfulServlet(IResourceProvider... theResourceProviders) {
		myResourceProviders = Arrays.asList(theResourceProviders);
	}

	@Override
	public Collection<IResourceProvider> getResourceProviders() {
		return myResourceProviders;
	}

    @Override
    public ISecurityManager getSecurityManager() {
        return null;
    }

}
