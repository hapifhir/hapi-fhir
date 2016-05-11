package example;

import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jaxrs.client.JaxRsRestfulClientFactory;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.HardcodedServerAddressStrategy;
import ca.uhn.fhir.rest.server.IResourceProvider;

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
