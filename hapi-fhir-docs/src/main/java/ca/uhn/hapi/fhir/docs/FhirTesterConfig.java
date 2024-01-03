/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.docs;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.to.FhirTesterMvcConfig;
import ca.uhn.fhir.to.TesterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

// START SNIPPET: file
/**
 * This spring config file configures the web testing module. It serves two
 * purposes:
 * 1. It imports FhirTesterMvcConfig, which is the spring config for the
 *    tester itself
 * 2. It tells the tester which server(s) to talk to, via the testerConfig()
 *    method below
 */
@Configuration
@Import(FhirTesterMvcConfig.class)
public class FhirTesterConfig {

	/**
	 * This bean tells the testing webpage which servers it should configure itself
	 * to communicate with. In this example we configure it to talk to the local
	 * server, as well as one public server. If you are creating a project to
	 * deploy somewhere else, you might choose to only put your own server's
	 * address here.
	 *
	 * Note the use of the ${serverBase} variable below. This will be replaced with
	 * the base URL as reported by the server itself. Often for a simple Tomcat
	 * (or other container) installation, this will end up being something
	 * like "http://localhost:8080/hapi-fhir-jpaserver-example". If you are
	 * deploying your server to a place with a fully qualified domain name,
	 * you might want to use that instead of using the variable.
	 */
	@Bean
	public TesterConfig testerConfig() {
		TesterConfig retVal = new TesterConfig();
		retVal.addServer()
				.withId("home")
				.withFhirVersion(FhirVersionEnum.R4)
				.withBaseUrl("${serverBase}/fhir")
				.withName("Local Tester")
				// Add a $diff button on search result rows where version > 1
				.withSearchResultRowOperation(
						"$diff", id -> id.isVersionIdPartValidLong() && id.getVersionIdPartAsLong() > 1)
				.addServer()
				.withId("hapi")
				.withFhirVersion(FhirVersionEnum.R4)
				.withBaseUrl("http://hapi.fhir.org/baseR4")
				.withName("Public HAPI Test Server")
				// Disable the read and update buttons on search result rows for this server
				.withSearchResultRowInteraction(RestOperationTypeEnum.READ, id -> false)
				.withSearchResultRowInteraction(RestOperationTypeEnum.UPDATE, id -> false);

		/*
		 * Use the method below to supply a client "factory" which can be used
		 * if your server requires authentication
		 */
		// retVal.setClientFactory(clientFactory);

		return retVal;
	}
}
// END SNIPPET: file
