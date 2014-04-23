package example;

import org.apache.http.impl.client.HttpClientBuilder;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.HttpBasicAuthInterceptor;
import ca.uhn.fhir.rest.client.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.api.IBasicClient;

public class ClientExamples {

	public interface PatientClient extends IBasicClient {
		// nothing yet
	}

	@SuppressWarnings("unused")
	public void createSecurity() {
//START SNIPPET: security
// Create a context and get the client factory so it can be configured
FhirContext ctx = new FhirContext();
IRestfulClientFactory clientFactory = ctx.getRestfulClientFactory();

// Create an HTTP Client Builder
HttpClientBuilder builder = HttpClientBuilder.create();

// This interceptor adds HTTP username/password to every request 
String username = "foobar";
String password = "boobear";
builder.addInterceptorFirst(new HttpBasicAuthInterceptor(username, password));

// Use the new HTTP client builder
clientFactory.setHttpClient(builder.build());

// Actually create a client instance
PatientClient client = ctx.newRestfulClient(PatientClient.class, "http://localhost:9999/");
//END SNIPPET: security

	}
	
}
