package example;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.client.JaxRsRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IGenericClient;

@SuppressWarnings(value= {"serial"})
public class JaxRsClient {

public static void main(String[] args) {
//START SNIPPET: createClient
   
   // Create a client
   FhirContext ctx = FhirContext.forDstu2();
   
   // Create an instance of the JAX RS client factory and
   // set it on the context
   JaxRsRestfulClientFactory clientFactory = new JaxRsRestfulClientFactory(ctx);
   ctx.setRestfulClientFactory(clientFactory);
   
   // This client uses JAX-RS!
   IGenericClient client = ctx.newRestfulGenericClient("http://fhirtest.uhn.ca/baseDstu2");
   
//END SNIPPET: createClient
}


}
