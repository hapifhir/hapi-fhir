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
package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.cr.r4.IActivityDefinitionProcessorFactory;
import ca.uhn.fhir.cr.r4.IPlanDefinitionProcessorFactory;
import ca.uhn.fhir.cr.r4.activitydefinition.ActivityDefinitionOperationsProvider;
import ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionOperationsProvider;
import org.opencds.cqf.cql.evaluator.activitydefinition.r4.ActivityDefinitionProcessor;
import org.opencds.cqf.cql.evaluator.plandefinition.r4.PlanDefinitionProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(SdcR4Config.class)
public class CpgR4Config extends BaseCpgConfig {
	@Bean
	IActivityDefinitionProcessorFactory r4ActivityDefinitionProcessorFactory() {
		return r -> new ActivityDefinitionProcessor(r);
	}

	@Bean
	public ActivityDefinitionOperationsProvider r4ActivityDefinitionOperationsProvider() {
		return new ActivityDefinitionOperationsProvider();
	}

	@Bean
	IPlanDefinitionProcessorFactory r4PlanDefinitionProcessorFactory() {
		return r -> new PlanDefinitionProcessor(r);
	}

	@Bean
	public PlanDefinitionOperationsProvider r4PlanDefinitionOperationsProvider() {
		return new PlanDefinitionOperationsProvider();
	}
}
