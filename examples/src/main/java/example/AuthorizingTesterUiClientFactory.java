package example;

import javax.servlet.http.HttpServletRequest;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ca.uhn.fhir.rest.server.util.ITestingUiClientFactory;

public class AuthorizingTesterUiClientFactory implements ITestingUiClientFactory {

   @Override
   public IGenericClient newClient(FhirContext theFhirContext, HttpServletRequest theRequest, String theServerBaseUrl) {
      // Create a client
      IGenericClient client = theFhirContext.newRestfulGenericClient(theServerBaseUrl);
      
      // Register an interceptor which adds credentials
      client.registerInterceptor(new BasicAuthInterceptor("someusername", "somepassword"));
      
      return client;
   }

}
