package example;

import java.util.Collection;

import javax.servlet.ServletException;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

@SuppressWarnings("serial")
public class Dstu2Examples {
   private Collection<IResourceProvider> resourceProviderList;

   public static void main(String[] args) {
      new Dstu2Examples().getResourceTags();
   }

   @SuppressWarnings("unused")
   public void getResourceTags() {
      // START SNIPPET: context
      // Create a "dev" context, which supports DSTU2
      FhirContext ctx = FhirContext.forDev();
      
      // This parser supports DSTU2
      IParser parser = ctx.newJsonParser();
      
      // This client supports DSTU2
      IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
      // END SNIPPET: context
   }

   
   // START SNIPPET: server
   public class MyServer extends RestfulServer
   {

      @Override
      protected void initialize() throws ServletException {

         // In your initialize method, assign a DEV FhirContext. This
         // is all that is required in order to put the server
         // into DSTU2 mode
         setFhirContext(FhirContext.forDev());
         
         // Then set resource providers as normal, and do any other
         // configuration you need to do.
         setResourceProviders(resourceProviderList);
         
      }
      
   }
   // END SNIPPET: server

}
