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

import ca.uhn.fhir.cr.dstu3.measure.MeasureOperationsProvider;
import ca.uhn.fhir.cr.dstu3.measure.MeasureService;
import ca.uhn.fhir.cr.dstu3.activitydefinition.ActivityDefinitionOperationsProvider;
import ca.uhn.fhir.cr.dstu3.plandefinition.PlanDefinitionOperationsProvider;
import ca.uhn.fhir.cr.dstu3.questionnaire.QuestionnaireOperationsProvider;
import ca.uhn.fhir.cr.dstu3.questionnaireresponse.QuestionnaireResponseOperationsProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.opencds.cqf.cql.evaluator.activitydefinition.dstu3.ActivityDefinitionProcessor;
import org.opencds.cqf.cql.evaluator.plandefinition.dstu3.PlanDefinitionProcessor;
import org.opencds.cqf.cql.evaluator.questionnaire.dstu3.QuestionnaireProcessor;
import org.opencds.cqf.cql.evaluator.questionnaireresponse.dstu3.QuestionnaireResponseProcessor;
import org.opencds.cqf.fhir.api.Repository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.function.Function;

@Configuration
public class CrDstu3Config extends BaseClinicalReasoningConfig {

	// Measure

	@Bean
	public Function<RequestDetails, MeasureService> dstu3MeasureServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var ms = theApplicationContext.getBean(MeasureService.class);
			ms.setRequestDetails(r);
			return ms;
		};
	}

	@Bean
	@Scope("prototype")
	public MeasureService dstu3measureService() {
		return new MeasureService();
	}

	@Bean
	public MeasureOperationsProvider dstu3measureOperationsProvider() {
		return new MeasureOperationsProvider();
	}

	// ActivityDefinition

	@FunctionalInterface
	public interface IDstu3ActivityDefinitionProcessorFactory {
		ActivityDefinitionProcessor create(Repository theRepository);
	}

	@Bean
	IDstu3ActivityDefinitionProcessorFactory dstu3ActivityDefinitionProcessorFactory() {
		return r -> new ActivityDefinitionProcessor(r);
	}

	@Bean
	public ActivityDefinitionOperationsProvider dstu3ActivityDefinitionOperationsProvider() {
		return new ActivityDefinitionOperationsProvider();
	}

	// PlanDefinition

	@FunctionalInterface
	public interface IDstu3PlanDefinitionProcessorFactory {
		PlanDefinitionProcessor create(Repository theRepository);
	}

	@Bean
	IDstu3PlanDefinitionProcessorFactory dstu3PlanDefinitionProcessorFactory() {
		return r -> new PlanDefinitionProcessor(r);
	}

	@Bean
	public PlanDefinitionOperationsProvider dstu3PlanDefinitionOperationsProvider() {
		return new PlanDefinitionOperationsProvider();
	}

	// Questionnaire

	@FunctionalInterface
	public interface IDstu3QuestionnaireProcessorFactory {
		QuestionnaireProcessor create(Repository theRepository);
	}

	@Bean
	IDstu3QuestionnaireProcessorFactory dstu3QuestionnaireProcessorFactory() {
		return r -> new QuestionnaireProcessor(r);
	}

	@Bean
	public QuestionnaireOperationsProvider dstu3QuestionnaireOperationsProvider() {
		return new QuestionnaireOperationsProvider();
	}

	// QuestionnaireResponse

	@FunctionalInterface
	public interface IDstu3QuestionnaireResponseProcessorFactory {
		QuestionnaireResponseProcessor create(Repository theRepository);
	}

	@Bean
	IDstu3QuestionnaireResponseProcessorFactory dstu3QuestionnaireResponseProcessorFactory() {
		return r -> new QuestionnaireResponseProcessor(r);
	}

	@Bean
	public QuestionnaireResponseOperationsProvider dstu3QuestionnaireResponseOperationsProvider() {
		return new QuestionnaireResponseOperationsProvider();
	}
}
