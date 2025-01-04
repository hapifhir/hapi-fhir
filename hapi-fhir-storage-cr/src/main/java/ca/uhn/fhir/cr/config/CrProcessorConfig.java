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
package ca.uhn.fhir.cr.config;

import ca.uhn.fhir.cr.common.IRepositoryFactory;
import org.opencds.cqf.fhir.cql.EvaluationSettings;
import org.opencds.cqf.fhir.cr.activitydefinition.ActivityDefinitionProcessor;
import org.opencds.cqf.fhir.cr.library.LibraryProcessor;
import org.opencds.cqf.fhir.cr.plandefinition.PlanDefinitionProcessor;
import org.opencds.cqf.fhir.cr.questionnaire.QuestionnaireProcessor;
import org.opencds.cqf.fhir.cr.questionnaireresponse.QuestionnaireResponseProcessor;
import org.opencds.cqf.fhir.cr.valueset.ValueSetProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CrProcessorConfig {
	@Bean
	ca.uhn.fhir.cr.common.IActivityDefinitionProcessorFactory activityDefinitionProcessorFactory(
			IRepositoryFactory theRepositoryFactory, EvaluationSettings theEvaluationSettings) {
		return rd -> new ActivityDefinitionProcessor(theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.common.IPlanDefinitionProcessorFactory planDefinitionProcessorFactory(
			IRepositoryFactory theRepositoryFactory, EvaluationSettings theEvaluationSettings) {
		return rd -> new PlanDefinitionProcessor(theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.common.IQuestionnaireProcessorFactory questionnaireProcessorFactory(
			IRepositoryFactory theRepositoryFactory, EvaluationSettings theEvaluationSettings) {
		return rd -> new QuestionnaireProcessor(theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.common.IQuestionnaireResponseProcessorFactory questionnaireResponseProcessorFactory(
			IRepositoryFactory theRepositoryFactory, EvaluationSettings theEvaluationSettings) {
		return rd -> new QuestionnaireResponseProcessor(theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.common.ILibraryProcessorFactory libraryProcessorFactory(
			IRepositoryFactory theRepositoryFactory, EvaluationSettings theEvaluationSettings) {
		return rd -> new LibraryProcessor(theRepositoryFactory.create(rd), theEvaluationSettings);
	}

	@Bean
	ca.uhn.fhir.cr.common.IValueSetProcessorFactory valueSetProcessorFactory(
			IRepositoryFactory theRepositoryFactory, EvaluationSettings theEvaluationSettings) {
		return rd -> new ValueSetProcessor(theRepositoryFactory.create(rd), theEvaluationSettings);
	}
}
