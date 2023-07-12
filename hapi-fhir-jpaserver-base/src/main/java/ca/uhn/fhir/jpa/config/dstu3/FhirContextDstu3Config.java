/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.config.dstu3;

import ca.uhn.fhir.context.FhirContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static ca.uhn.fhir.jpa.config.r4.FhirContextR4Config.configureFhirContext;

public class FhirContextDstu3Config {
	@Primary
	@Bean(name = "primaryFhirContext")
	public FhirContext fhirContextDstu3() {
		FhirContext retVal = FhirContext.forDstu3();

		configureFhirContext(retVal);

		return retVal;
	}
}
