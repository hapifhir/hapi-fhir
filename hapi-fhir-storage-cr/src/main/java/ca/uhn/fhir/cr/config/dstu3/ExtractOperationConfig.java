/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
package ca.uhn.fhir.cr.config.dstu3;

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
public class ExtractOperationConfig {
	@Bean
	ca.uhn.fhir.cr.dstu3.questionnaireresponse.QuestionnaireResponseExtractProvider
			dstu3QuestionnaireResponseExtractProvider() {
		return new ca.uhn.fhir.cr.dstu3.questionnaireresponse.QuestionnaireResponseExtractProvider();
	}

	@Bean(name = "extractOperationLoader")
	public ProviderLoader extractOperationLoader(
			ApplicationContext theApplicationContext, FhirContext theFhirContext, RestfulServer theRestfulServer) {
		var selector = new ProviderSelector(
				theFhirContext,
				Map.of(
						FhirVersionEnum.DSTU3,
						Arrays.asList(
								ca.uhn.fhir.cr.dstu3.questionnaireresponse.QuestionnaireResponseExtractProvider
										.class)));

		return new ProviderLoader(theRestfulServer, theApplicationContext, selector);
	}
}
