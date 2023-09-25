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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.Map;

public class ApplyOperationConfig {
	@Autowired(required = false)
	EvaluationSettings myEvaluationSettings;
	@Autowired(required = false)
	private RestfulServer myRestfulServer;

	@Bean
	ca.uhn.fhir.cr.r4.IActivityDefinitionProcessorFactory r4ActivityDefinitionProcessorFactory(
			IRepositoryFactory theRepositoryFactory) {
		if (myEvaluationSettings == null) {
			return null;
		}
		return rd -> new org.opencds.cqf.fhir.cr.activitydefinition.r4.ActivityDefinitionProcessor(
				theRepositoryFactory.create(rd), myEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.r4.IPlanDefinitionProcessorFactory r4PlanDefinitionProcessorFactory(
			IRepositoryFactory theRepositoryFactory) {
		if (myEvaluationSettings == null) {
			return null;
		}
		return rd -> new org.opencds.cqf.fhir.cr.plandefinition.r4.PlanDefinitionProcessor(
				theRepositoryFactory.create(rd), myEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.r4.activitydefinition.ActivityDefinitionApplyProvider r4ActivityDefinitionApplyProvider() {
		return new ca.uhn.fhir.cr.r4.activitydefinition.ActivityDefinitionApplyProvider();
	}

	@Bean
	ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionApplyProvider r4PlanDefinitionApplyProvider() {
		return new ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionApplyProvider();
	}

	@Bean(name = "applyOperationLoader")
	public ProviderLoader applyOperationLoader(ApplicationContext theApplicationContext, FhirContext theFhirContext) {
		if (myRestfulServer == null) {
			return null;
		}
		var selector = new ProviderSelector(
				theFhirContext,
				Map.of(
						FhirVersionEnum.R4,
						Arrays.asList(
								ca.uhn.fhir.cr.r4.activitydefinition.ActivityDefinitionApplyProvider.class,
								ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionApplyProvider.class)));

		return new ProviderLoader(myRestfulServer, theApplicationContext, selector);
	}
}
