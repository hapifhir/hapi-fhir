package ca.uhn.example.rest;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.annotation.WebServlet;

import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

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
		// Use a narrative generator
		setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());
	}
	
	/**
	 * Restful servers must provide an implementation of this method, which
	 * returns all resource providers to be used by this server. In the example
	 * below, we are creating a RESTful server which is able to serve
	 * Patient and Observation resources.
	 */
	@Override
	public Collection<IResourceProvider> getResourceProviders() {
		List<IResourceProvider> retVal = new ArrayList<IResourceProvider>();
		retVal.add(new RestfulPatientResourceProvider());
		retVal.add(new RestfulObservationResourceProvider());
		return retVal;
	}

}
