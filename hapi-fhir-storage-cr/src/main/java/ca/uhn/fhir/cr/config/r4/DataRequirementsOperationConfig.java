/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.cr.config.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.cr.config.CrProcessorConfig;
import ca.uhn.fhir.cr.config.ProviderLoader;
import ca.uhn.fhir.cr.config.ProviderSelector;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Arrays;
import java.util.Map;

@Configuration
@Import(CrProcessorConfig.class)
public class DataRequirementsOperationConfig {
	@Bean
	ca.uhn.fhir.cr.r4.library.LibraryDataRequirementsProvider r4LibraryDataRequirementsProvider() {
		return new ca.uhn.fhir.cr.r4.library.LibraryDataRequirementsProvider();
	}

	@Bean
	ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionDataRequirementsProvider r4PlanDefinitionDataRequirementsProvider() {
		return new ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionDataRequirementsProvider();
	}

	@Bean
	ca.uhn.fhir.cr.r4.questionnaire.QuestionnaireDataRequirementsProvider r4QuestionnaireDataRequirementsProvider() {
		return new ca.uhn.fhir.cr.r4.questionnaire.QuestionnaireDataRequirementsProvider();
	}

	@Bean
	ca.uhn.fhir.cr.r4.valueset.ValueSetDataRequirementsProvider r4ValueSetDataRequirementsProvider() {
		return new ca.uhn.fhir.cr.r4.valueset.ValueSetDataRequirementsProvider();
	}

	@Bean(name = "dataRequirementsOperationLoader")
	public ProviderLoader dataRequirementsOperationLoader(
			ApplicationContext theApplicationContext, FhirContext theFhirContext, RestfulServer theRestfulServer) {
		var selector = new ProviderSelector(
				theFhirContext,
				Map.of(
						FhirVersionEnum.R4,
						Arrays.asList(
								ca.uhn.fhir.cr.r4.library.LibraryDataRequirementsProvider.class,
								ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionDataRequirementsProvider.class,
								ca.uhn.fhir.cr.r4.questionnaire.QuestionnaireDataRequirementsProvider.class,
								ca.uhn.fhir.cr.r4.valueset.ValueSetDataRequirementsProvider.class)));

		return new ProviderLoader(theRestfulServer, theApplicationContext, selector);
	}
}
