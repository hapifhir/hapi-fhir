/*-
 * #%L
 * HAPI FHIR JPA Server
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
package ca.uhn.fhir.jpa.config.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.context.ParserOptions;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

public class FhirContextR4Config {

	public static final String DEFAULT_PRESERVE_VERSION_REFS_DSTU2 = "AuditEvent.object.reference";
	public static final String DEFAULT_PRESERVE_VERSION_REFS_DSTU3 = "AuditEvent.entity.reference";
	public static final String DEFAULT_PRESERVE_VERSION_REFS_R4_AND_LATER = "AuditEvent.entity.what";

	@Bean(name = "primaryFhirContext")
	@Primary
	public FhirContext fhirContextR4() {
		FhirContext retVal = FhirContext.forR4();

		configureFhirContext(retVal);

		return retVal;
	}

	public static FhirContext configureFhirContext(FhirContext theFhirContext) {
		// Don't strip versions in some places
		ParserOptions parserOptions = theFhirContext.getParserOptions();
		if (theFhirContext.getVersion().getVersion().isOlderThan(FhirVersionEnum.DSTU3)) {
			parserOptions.setDontStripVersionsFromReferencesAtPaths(DEFAULT_PRESERVE_VERSION_REFS_DSTU2);
		} else if (theFhirContext.getVersion().getVersion().equals(FhirVersionEnum.DSTU3)) {
			parserOptions.setDontStripVersionsFromReferencesAtPaths(DEFAULT_PRESERVE_VERSION_REFS_DSTU3);
		} else {
			parserOptions.setDontStripVersionsFromReferencesAtPaths(DEFAULT_PRESERVE_VERSION_REFS_R4_AND_LATER);
		}

		// We use this context to create subscription deliveries and that kind of thing. It doesn't
		// make much sense to let the HTTP client pool be a blocker since we have delivery queue
		// sizing as the lever to affect that. So make the pool big enough that it shouldn't get
		// in the way.
		IRestfulClientFactory clientFactory = theFhirContext.getRestfulClientFactory();
		clientFactory.setPoolMaxPerRoute(1000);
		clientFactory.setPoolMaxTotal(1000);

		return theFhirContext;
	}
}
