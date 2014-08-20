package example;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

//START SNIPPET: servlet
/**
 * In this example, we are using Servlet 3.0 annotations to define
 * the URL pattern for this servlet, but we could also
 * define this in a web.xml file.
 */
@WebServlet(urlPatterns= {"/fhir/*"}, displayName="FHIR Server")
public class ExampleRestfulServlet extends RestfulServer {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public ExampleRestfulServlet() {
		/*
		 * The servlet defines any number of resource providers, and
		 * configures itself to use them by calling
		 * setResourceProviders()
		 */
		List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
		resourceProviders.add(new RestfulPatientResourceProvider());
		resourceProviders.add(new RestfulObservationResourceProvider());
		setResourceProviders(resourceProviders);
	}
	
}
//END SNIPPET: servlet
