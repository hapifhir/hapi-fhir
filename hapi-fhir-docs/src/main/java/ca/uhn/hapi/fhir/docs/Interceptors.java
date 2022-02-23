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
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.hl7.fhir.r4.model.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Interceptors {


	// START SNIPPET: sampleClass
	@Interceptor
	public class SimpleServerLoggingInterceptor {

		private final Logger ourLog = LoggerFactory.getLogger(SimpleServerLoggingInterceptor.class);

		@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_HANDLED)
		public void logRequests(RequestDetails theRequest) {
			ourLog.info("Request of type {} with request ID: {}", theRequest.getOperation(), theRequest.getRequestId());
		}

	}
	// END SNIPPET: sampleClass


	public void registerClient() {

		// START SNIPPET: registerClient
		FhirContext ctx = FhirContext.forR4();

		// Create a new client instance
		IGenericClient client = ctx.newRestfulGenericClient("http://hapi.fhir.org/baseR4");

		// Register an interceptor against the client
		client.registerInterceptor(new LoggingInterceptor());

		// Perform client actions...
		Patient pt = client.read().resource(Patient.class).withId("example").execute();
		// END SNIPPET: registerClient

	}

}
