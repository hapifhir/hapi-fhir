package example;

import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

@SuppressWarnings({ "serial" })
//START SNIPPET: provider
public class PagingServer extends RestfulServer {

	public PagingServer() {
		
		/*
		 * Set the resource providers as always. Here we are using the paging
		 * provider from the example below, but it is not strictly neccesary
		 * to use a paging resource provider as well. If a normal resource 
		 * provider is used (one which returns List<?> instead of IBundleProvider)
		 * then the loaded resources will be stored by the IPagingProvider.
		 */
		setResourceProviders(new PagingPatientProvider());
		
		/*
		 * Set a paging provider. Here a simple in-memory implementation
		 * is used, but you may create your own. 
		 */
		FifoMemoryPagingProvider pp = new FifoMemoryPagingProvider(10);
		pp.setDefaultPageSize(10);
		pp.setMaximumPageSize(100);
		setPagingProvider(pp);
				
	}

}
//END SNIPPET: provider
