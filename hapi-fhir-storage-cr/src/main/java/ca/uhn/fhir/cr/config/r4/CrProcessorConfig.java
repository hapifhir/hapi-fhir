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
package ca.uhn.fhir.cr.config.r4;

import ca.uhn.fhir.cr.common.IRepositoryFactory;
import org.opencds.cqf.fhir.cql.EvaluationSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrProcessorConfig {
	@Bean
	ca.uhn.fhir.cr.r4.IActivityDefinitionProcessorFactory r4ActivityDefinitionProcessorFactory(
			IRepositoryFactory theRepositoryFactory, EvaluationSettings theEvaluationSettings) {
		return rd -> new org.opencds.cqf.fhir.cr.activitydefinition.r4.ActivityDefinitionProcessor(
				theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.r4.IPlanDefinitionProcessorFactory r4PlanDefinitionProcessorFactory(
			IRepositoryFactory theRepositoryFactory, EvaluationSettings theEvaluationSettings) {
		return rd -> new org.opencds.cqf.fhir.cr.plandefinition.r4.PlanDefinitionProcessor(
				theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.r4.IQuestionnaireProcessorFactory r4QuestionnaireProcessorFactory(
			IRepositoryFactory theRepositoryFactory, EvaluationSettings theEvaluationSettings) {
		return rd -> new org.opencds.cqf.fhir.cr.questionnaire.r4.processor.QuestionnaireProcessor(
				theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.r4.IQuestionnaireResponseProcessorFactory r4QuestionnaireResponseProcessorFactory(
			IRepositoryFactory theRepositoryFactory, EvaluationSettings theEvaluationSettings) {
		return rd -> new org.opencds.cqf.fhir.cr.questionnaireresponse.r4.QuestionnaireResponseProcessor(
				theRepositoryFactory.create(rd), theEvaluationSettings);
	}
}
