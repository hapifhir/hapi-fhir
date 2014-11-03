package example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

@SuppressWarnings(value= {"serial","unused"})
public class ExampleProviders {

	
//START SNIPPET: plainProvider
public class PlainProvider {

  /**
   * This method is a Patient search, but HAPI can not automatically
   * determine the resource type so it must be explicitly stated.
   */
  @Search(type=Patient.class)
  public Bundle searchForPatients(@RequiredParam(name="name") StringDt theName) {
    Bundle retVal = new Bundle();
    // perform search
    return retVal;
  }	
	
}
//END SNIPPET: plainProvider


//START SNIPPET: plainProviderServer
public class ExampleServlet extends RestfulServer {

  public ExampleServlet() {
    /*
     * Plain providers are passed to the server in the same way
     * as resource providers. You may pass both resource providers
     * and and plain providers to the same server if you like. 
     */
    List<Object> plainProviders=new ArrayList<Object>();
    plainProviders.add(new PlainProvider());
    setPlainProviders(plainProviders);
    
    List<IResourceProvider> resourceProviders = new ArrayList<IResourceProvider>();
    // ...add some resource providers...
    setResourceProviders(resourceProviders);
  }
	
}
//END SNIPPET: plainProviderServer


}
