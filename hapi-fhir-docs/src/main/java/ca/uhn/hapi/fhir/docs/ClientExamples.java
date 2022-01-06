package ca.uhn.hapi.fhir.docs;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.okhttp.client.OkHttpRestfulClientFactory;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.apache.GZipContentInterceptor;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import ca.uhn.fhir.rest.client.interceptor.AdditionalRequestHeadersInterceptor;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.client.interceptor.CookieInterceptor;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.client.interceptor.UrlTenantSelectionInterceptor;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

public class ClientExamples {

   public interface IPatientClient extends IBasicClient {
      // nothing yet
   }

   @SuppressWarnings("unused")
   public void createProxy() {
      // START SNIPPET: proxy
      FhirContext ctx = FhirContext.forR4();

      // Set connections to access the network via the HTTP proxy at
      // example.com : 8888
      ctx.getRestfulClientFactory().setProxy("example.com", 8888);

      // If the proxy requires authentication, use the following as well
      ctx.getRestfulClientFactory().setProxyCredentials("theUsername", "thePassword");
      
      // Create the client
      IGenericClient genericClient = ctx.newRestfulGenericClient("http://localhost:9999/fhir");
      // END SNIPPET: proxy
   }


   public void tenantId() {
   	// START SNIPPET: tenantId
		FhirContext ctx = FhirContext.forR4();

		// Create the client
		IGenericClient genericClient = ctx.newRestfulGenericClient("http://localhost:9999/fhir");

		// Register the interceptor
		UrlTenantSelectionInterceptor tenantSelection = new UrlTenantSelectionInterceptor();
		genericClient.registerInterceptor(tenantSelection);

		// Read from tenant A
		tenantSelection.setTenantId("TENANT-A");
		Patient patientA = genericClient.read().resource(Patient.class).withId("123").execute();

		// Read from tenant B
		tenantSelection.setTenantId("TENANT-B");
		Patient patientB = genericClient.read().resource(Patient.class).withId("456").execute();
		// END SNIPPET: tenantId
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
      FhirContext ctx = FhirContext.forR4();

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
      FhirContext ctx = FhirContext.forR4();
      IRestfulClientFactory clientFactory = ctx.getRestfulClientFactory();

      // Create an HTTP basic auth interceptor
      String username = "foobar";
      String password = "boobear";
      IClientInterceptor authInterceptor = new BasicAuthInterceptor(username, password);

		// If you're using an annotation client, use this style to
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
      FhirContext ctx = FhirContext.forR4();
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
	public void addHeaders() {
		// START SNIPPET: addHeaders
		// Create a context and get the client factory so it can be configured
		FhirContext ctx = FhirContext.forR4();
		IRestfulClientFactory clientFactory = ctx.getRestfulClientFactory();

		// Create a client
		IGenericClient client = ctx.newRestfulGenericClient( "http://localhost:9999/fhir");

		// Register an additional headers interceptor and add one header to it
		AdditionalRequestHeadersInterceptor interceptor = new AdditionalRequestHeadersInterceptor();
		interceptor.addHeaderValue("X-Message", "Help I'm a Bug");
		client.registerInterceptor(interceptor);

		IGenericClient genericClient = ctx.newRestfulGenericClient("http://localhost:9999/fhir");
		client.registerInterceptor(interceptor);
		// END SNIPPET: addHeaders

		// START SNIPPET: addHeadersNoInterceptor
		Patient p = client
			.read()
			.resource(Patient.class)
			.withId(123L)
			.withAdditionalHeader("X-Message", "Help I'm a Bug")
			.execute();
		// END SNIPPET: addHeadersNoInterceptor
	}

   @SuppressWarnings("unused")
   public void gzip() {
      // START SNIPPET: gzip
      // Create a context and get the client factory so it can be configured
      FhirContext ctx = FhirContext.forR4();
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
      FhirContext ctx = FhirContext.forR4();
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
         FhirContext ctx = FhirContext.forR4();
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
         FhirContext ctx = FhirContext.forR4();
         IPatientClient client = ctx.newRestfulClient(IPatientClient.class, "http://localhost:9999/");

         // Request JSON encoding from the server (_format=json)
         client.setEncoding(EncodingEnum.JSON);

         // Request pretty printing from the server (_pretty=true)
         client.setPrettyPrint(true);
         // END SNIPPET: clientConfig
      }
   }

}
