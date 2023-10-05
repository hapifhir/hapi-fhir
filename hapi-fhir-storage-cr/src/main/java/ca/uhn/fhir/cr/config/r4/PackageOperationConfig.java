/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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
package ca.uhn.fhir.cr.config.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.cr.common.IRepositoryFactory;
import ca.uhn.fhir.cr.config.ProviderLoader;
import ca.uhn.fhir.cr.config.ProviderSelector;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.opencds.cqf.fhir.cql.EvaluationSettings;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Map;

@Configuration
@ConditionalOnBean({IRepositoryFactory.class, RestfulServer.class, EvaluationSettings.class})
public class PackageOperationConfig {
	@Bean
	ca.uhn.fhir.cr.r4.IPlanDefinitionProcessorFactory r4PlanDefinitionProcessorFactory(
			IRepositoryFactory theRepositoryFactory, EvaluationSettings theEvaluationSettings) {
		return rd -> new org.opencds.cqf.fhir.cr.plandefinition.r4.PlanDefinitionProcessor(
				theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionPackageProvider r4PlanDefinitionPackageProvider() {
		return new ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionPackageProvider();
	}

	@Bean
	ca.uhn.fhir.cr.r4.IQuestionnaireProcessorFactory r4QuestionnaireProcessorFactory(
			IRepositoryFactory theRepositoryFactory, EvaluationSettings theEvaluationSettings) {
		return rd -> new org.opencds.cqf.fhir.cr.questionnaire.r4.QuestionnaireProcessor(
				theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.r4.questionnaire.QuestionnairePackageProvider r4QuestionnairePackageProvider() {
		return new ca.uhn.fhir.cr.r4.questionnaire.QuestionnairePackageProvider();
	}

	@Bean(name = "packageOperationLoader")
	public ProviderLoader packageOperationLoader(
			ApplicationContext theApplicationContext, FhirContext theFhirContext, RestfulServer theRestfulServer) {
		var selector = new ProviderSelector(
				theFhirContext,
				Map.of(
						FhirVersionEnum.R4,
						Arrays.asList(
								ca.uhn.fhir.cr.r4.questionnaire.QuestionnairePackageProvider.class,
								ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionPackageProvider.class)));

		return new ProviderLoader(theRestfulServer, theApplicationContext, selector);
	}
}
