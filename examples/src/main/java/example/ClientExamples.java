package example;

import ca.uhn.fhir.rest.api.CacheControlDirective;
import org.hl7.fhir.dstu3.model.Bundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.okhttp.client.OkHttpRestfulClientFactory;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.apache.GZipContentInterceptor;
import ca.uhn.fhir.rest.client.api.*;
import ca.uhn.fhir.rest.client.interceptor.*;
import org.hl7.fhir.r4.model.Patient;

public class ClientExamples {

   public interface IPatientClient extends IBasicClient {
      // nothing yet
   }

   @SuppressWarnings("unused")
   public void createProxy() {
      // START SNIPPET: proxy
      FhirContext ctx = FhirContext.forDstu2();

      // Set connections to access the network via the HTTP proxy at
      // example.com : 8888
      ctx.getRestfulClientFactory().setProxy("example.com", 8888);

      // If the proxy requires authentication, use the following as well
      ctx.getRestfulClientFactory().setProxyCredentials("theUsername", "thePassword");
      
      // Create the client
      IGenericClient genericClient = ctx.newRestfulGenericClient("http://localhost:9999/fhir");
      // END SNIPPET: proxy
   }

   @SuppressWarnings("unused")
   public void processMessage() {
      // START SNIPPET: processMessage
      FhirContext ctx = FhirContext.forDstu3();

      // Create the client
      IGenericClient client = ctx.newRestfulGenericClient("http://localhost:9999/fhir");
      
      Bundle bundle = new Bundle();
      // ..populate the bundle..
      
      Bundle response = client
            .operation()
            .processMessage() // New operation for sending messages
            .setMessageBundle(bundle)
            .asynchronous(Bundle.class)
            .execute();
      // END SNIPPET: processMessage
   }

	@SuppressWarnings("unused")
	public void cacheControl() {
		FhirContext ctx = FhirContext.forDstu3();

		// Create the client
		IGenericClient client = ctx.newRestfulGenericClient("http://localhost:9999/fhir");

		Bundle bundle = new Bundle();
		// ..populate the bundle..

		// START SNIPPET: cacheControl
		Bundle response = client
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.cacheControl(new CacheControlDirective().setNoCache(true)) // <-- add a directive
			.execute();
		// END SNIPPET: cacheControl
	}

   @SuppressWarnings("unused")
   public void createOkHttp() {
      // START SNIPPET: okhttp
      FhirContext ctx = FhirContext.forDstu3();

      // Use OkHttp
      ctx.setRestfulClientFactory(new OkHttpRestfulClientFactory(ctx));
      
      // Create the client
      IGenericClient genericClient = ctx.newRestfulGenericClient("http://localhost:9999/fhir");
      // END SNIPPET: okhttp
   }

   @SuppressWarnings("unused")
   public void createTimeouts() {
      // START SNIPPET: timeouts
      FhirContext ctx = FhirContext.forDstu2();

      // Set how long to try and establish the initial TCP connection (in ms)
      ctx.getRestfulClientFactory().setConnectTimeout(20 * 1000);
      
      // Set how long to block for individual read/write operations (in ms)
      ctx.getRestfulClientFactory().setSocketTimeout(20 * 1000);
      
      // Create the client
      IGenericClient genericClient = ctx.newRestfulGenericClient("http://localhost:9999/fhir");
      // END SNIPPET: timeouts
   }

   @SuppressWarnings("unused")
   public void createSecurity() {
      // START SNIPPET: security
      // Create a context and get the client factory so it can be configured
      FhirContext ctx = FhirContext.forDstu2();
      IRestfulClientFactory clientFactory = ctx.getRestfulClientFactory();

      // Create an HTTP basic auth interceptor
      String username = "foobar";
      String password = "boobear";
      IClientInterceptor authInterceptor = new BasicAuthInterceptor(username, password);

		// If you're usinf an annotation client, use this style to
		// register it
      IPatientClient annotationClient = ctx.newRestfulClient(IPatientClient.class, "http://localhost:9999/fhir");
		annotationClient.registerInterceptor(authInterceptor);

		// If you're using a generic client, use this instead
      IGenericClient genericClient = ctx.newRestfulGenericClient("http://localhost:9999/fhir");
      genericClient.registerInterceptor(authInterceptor);
      // END SNIPPET: security
   }

   @SuppressWarnings("unused")
   public void createCookie() {
      // START SNIPPET: cookie
      // Create a context and get the client factory so it can be configured
      FhirContext ctx = FhirContext.forDstu2();
      IRestfulClientFactory clientFactory = ctx.getRestfulClientFactory();

      // Create a cookie interceptor. This cookie will have the name "mycookie" and
      // the value "Chips Ahoy"
      CookieInterceptor interceptor = new CookieInterceptor("mycookie=Chips Ahoy");

      // Register the interceptor with your client (either style)
      IPatientClient annotationClient = ctx.newRestfulClient(IPatientClient.class, "http://localhost:9999/fhir");
      annotationClient.registerInterceptor(interceptor);

      IGenericClient genericClient = ctx.newRestfulGenericClient("http://localhost:9999/fhir");
      annotationClient.registerInterceptor(interceptor);
      // END SNIPPET: cookie
   }

   @SuppressWarnings("unused")
   public void gzip() {
      // START SNIPPET: gzip
      // Create a context and get the client factory so it can be configured
      FhirContext ctx = FhirContext.forDstu2();
      IRestfulClientFactory clientFactory = ctx.getRestfulClientFactory();

      // Register the interceptor with your client (either style)
      IPatientClient annotationClient = ctx.newRestfulClient(IPatientClient.class, "http://localhost:9999/fhir");
      annotationClient.registerInterceptor(new GZipContentInterceptor());
      // END SNIPPET: gzip
   }
   
   @SuppressWarnings("unused")
   public void createSecurityBearer() {
      // START SNIPPET: securityBearer
      // Create a context and get the client factory so it can be configured
      FhirContext ctx = FhirContext.forDstu2();
      IRestfulClientFactory clientFactory = ctx.getRestfulClientFactory();

      // In reality the token would have come from an authorization server 
      String token = "3w03fj.r3r3t"; 
      
      BearerTokenAuthInterceptor authInterceptor = new BearerTokenAuthInterceptor(token);

      // Register the interceptor with your client (either style)
      IPatientClient annotationClient = ctx.newRestfulClient(IPatientClient.class, "http://localhost:9999/fhir");
      annotationClient.registerInterceptor(authInterceptor);

      IGenericClient genericClient = ctx.newRestfulGenericClient("http://localhost:9999/fhir");
      annotationClient.registerInterceptor(authInterceptor);
      // END SNIPPET: securityBearer
   }

   @SuppressWarnings("unused")
   public void createLogging() {
      {
         // START SNIPPET: logging
         // Create a context and get the client factory so it can be configured
         FhirContext ctx = FhirContext.forDstu2();
         IRestfulClientFactory clientFactory = ctx.getRestfulClientFactory();

         // Create a logging interceptor
         LoggingInterceptor loggingInterceptor = new LoggingInterceptor();

         // Optionally you may configure the interceptor (by default only
         // summary info is logged)
         loggingInterceptor.setLogRequestSummary(true);
         loggingInterceptor.setLogRequestBody(true);

         // Register the interceptor with your client (either style)
         IPatientClient annotationClient = ctx.newRestfulClient(IPatientClient.class, "http://localhost:9999/fhir");
         annotationClient.registerInterceptor(loggingInterceptor);

         IGenericClient genericClient = ctx.newRestfulGenericClient("http://localhost:9999/fhir");
         genericClient.registerInterceptor(loggingInterceptor);
         // END SNIPPET: logging
      }

      /******************************/
      {
         // START SNIPPET: clientConfig
         // Create a client
         FhirContext ctx = FhirContext.forDstu2();
         IPatientClient client = ctx.newRestfulClient(IPatientClient.class, "http://localhost:9999/");

         // Request JSON encoding from the server (_format=json)
         client.setEncoding(EncodingEnum.JSON);

         // Request pretty printing from the server (_pretty=true)
         client.setPrettyPrint(true);
         // END SNIPPET: clientConfig
      }
   }

}
