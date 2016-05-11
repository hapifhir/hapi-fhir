package ca.uhn.example.servlet;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.example.provider.OrganizationResourceProvider;
import ca.uhn.example.provider.PatientResourceProvider;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.ResponseHighlighterInterceptor;

/**
 * This servlet is the actual FHIR server itself
 */
public class ExampleRestfulServlet extends RestfulServer {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public ExampleRestfulServlet() {
		super(FhirContext.forDstu2()); // Support DSTU2
	}
	
	/**
	 * This method is called automatically when the
	 * servlet is initializing.
	 */
	@Override
	public void initialize() {
		/*
		 * Two resource providers are defined. Each one handles a specific
		 * type of resource.
		 */
		List<IResourceProvider> providers = new ArrayList<IResourceProvider>();
		providers.add(new PatientResourceProvider());
		providers.add(new OrganizationResourceProvider());
		setResourceProviders(providers);
		
		/*
		 * Use a narrative generator. This is a completely optional step, 
		 * but can be useful as it causes HAPI to generate narratives for
		 * resources which don't otherwise have one.
		 */
		INarrativeGenerator narrativeGen = new DefaultThymeleafNarrativeGenerator();
		getFhirContext().setNarrativeGenerator(narrativeGen);

		/*
		 * This server interceptor causes the server to return nicely
		 * formatter and coloured responses instead of plain JSON/XML if
		 * the request is coming from a browser window. It is optional,
		 * but can be nice for testing.
		 */
		registerInterceptor(new ResponseHighlighterInterceptor());
		
		/*
		 * Tells the server to return pretty-printed responses by default
		 */
		setDefaultPrettyPrint(true);
		
	}

}
