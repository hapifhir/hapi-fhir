package ca.uhn.fhir.jpa.subscription.module.standalone;

/*-
 * #%L
 * HAPI FHIR Subscription Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// FIXME: rename
@Configuration
public class StandaloneSubscriptionConfig {

	/**
	 * Constructor
	 */
	public StandaloneSubscriptionConfig() {
		super();
	}

	@Autowired
	private IGenericClient myClient;

	@Bean
	public FhirClientResourceRetriever fhirClientResourceRetriever() {
		return new FhirClientResourceRetriever();
	}

	@Bean
	public FhirClientSearchParamProvider fhirClientSearchParamProvider() {
		return new FhirClientSearchParamProvider(myClient);
	}

	@Bean
	public FhirClientSubscriptionProvider fhirClientSubscriptionProvider() {
		return new FhirClientSubscriptionProvider(myClient);
	}

	@Bean
	public StandaloneSubscriptionMessageHandler fhirClientSubscriptionMessageHandler() {
		return new StandaloneSubscriptionMessageHandler();
	}

}
