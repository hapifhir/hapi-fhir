package embedded;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;

@Singleton
public class FhirRestfulServlet extends RestfulServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3931111342737918913L;

	public FhirRestfulServlet() {
		super(FhirContext.forDstu2()); // Support DSTU2
	}

	/**
	 * This method is called automatically when the servlet is initializing.
	 */
	@Override
	public void initialize() {
		final List<IResourceProvider> providers = new ArrayList<IResourceProvider>();
		providers.add(new SomeResourceProvider());
		setResourceProviders(providers);


		registerInterceptor(new ResponseHighlighterInterceptor());

	}
}
