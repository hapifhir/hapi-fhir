package ca.uhn.fhir.server;

import java.util.Arrays;
import java.util.Collection;

import ca.uhn.fhir.server.IResourceProvider;
import ca.uhn.fhir.server.RestfulServer;

public class DummyRestfulServer extends RestfulServer {

	private static final long serialVersionUID = 1L;
	
	private Collection<IResourceProvider<?>> myResourceProviders;

	public DummyRestfulServer(IResourceProvider<?>... theResourceProviders) {
		myResourceProviders = Arrays.asList(theResourceProviders);
	}

	@Override
	public Collection<IResourceProvider<?>> getResourceProviders() {
		return myResourceProviders;
	}

}
