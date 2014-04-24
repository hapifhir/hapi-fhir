package ca.uhn.example.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.narrative.INarrativeGenerator;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

/**
 * In this example, we are using Servlet 3.0 annotations to define the URL pattern for this servlet, but we could also define this in a web.xml file.
 */
@WebServlet(urlPatterns = { "/fhir/*" }, displayName = "FHIR Server")
public class ExampleRestfulServlet extends RestfulServer {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public ExampleRestfulServlet() {
		
		/*
		 * Two resource providers are defined
		 */
		List<IResourceProvider> providers = new ArrayList<IResourceProvider>();
		providers.add(new RestfulPatientResourceProvider());
		providers.add(new RestfulOrganizationResourceProvider());
		setResourceProviders(providers);
		
		/*
		 * Use a narrative generator
		 */
		INarrativeGenerator narrativeGen = new DefaultThymeleafNarrativeGenerator();
		getFhirContext().setNarrativeGenerator(narrativeGen);

	}

}
