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

import ca.uhn.fhir.cr.r4.activitydefinition.ActivityDefinitionOperationsProvider;
import ca.uhn.fhir.cr.r4.measure.MeasureOperationsProvider;
import ca.uhn.fhir.cr.r4.measure.MeasureService;
import ca.uhn.fhir.cr.r4.plandefinition.PlanDefinitionOperationsProvider;
import ca.uhn.fhir.cr.r4.questionnaire.QuestionnaireOperationsProvider;
import ca.uhn.fhir.cr.r4.questionnaireresponse.QuestionnaireResponseOperationsProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.opencds.cqf.cql.evaluator.activitydefinition.r4.ActivityDefinitionProcessor;
import org.opencds.cqf.cql.evaluator.plandefinition.r4.PlanDefinitionProcessor;
import org.opencds.cqf.cql.evaluator.questionnaire.r4.QuestionnaireProcessor;
import org.opencds.cqf.cql.evaluator.questionnaireresponse.r4.QuestionnaireResponseProcessor;
import org.opencds.cqf.fhir.api.Repository;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.function.Function;

@Configuration
public class CrR4Config extends BaseClinicalReasoningConfig {

	// Measure

	@Bean
	public Function<RequestDetails, MeasureService> r4MeasureServiceFactory(ApplicationContext theApplicationContext) {
		return r -> {
			var ms = theApplicationContext.getBean(MeasureService.class);
			ms.setRequestDetails(r);
			return ms;
		};
	}

	@Bean
	@Scope("prototype")
	public MeasureService r4measureService() {
		return new MeasureService();
	}

	@Bean
	public MeasureOperationsProvider r4measureOperationsProvider() {
		return new MeasureOperationsProvider();
	}

	// ActivityDefinition

	@FunctionalInterface
	public interface IR4ActivityDefinitionProcessorFactory {
		ActivityDefinitionProcessor create(Repository theRepository);
	}

	@Bean
	IR4ActivityDefinitionProcessorFactory r4ActivityDefinitionProcessorFactory() {
		return r -> new ActivityDefinitionProcessor(r);
	}

	@Bean
	public ActivityDefinitionOperationsProvider r4ActivityDefinitionOperationsProvider() {
		return new ActivityDefinitionOperationsProvider();
	}

	// PlanDefinition

	@FunctionalInterface
	public interface IR4PlanDefinitionProcessorFactory {
		PlanDefinitionProcessor create(Repository theRepository);
	}

	@Bean
	IR4PlanDefinitionProcessorFactory r4PlanDefinitionProcessorFactory() {
		return r -> new PlanDefinitionProcessor(r);
	}

	@Bean
	public PlanDefinitionOperationsProvider r4PlanDefinitionOperationsProvider() {
		return new PlanDefinitionOperationsProvider();
	}

	// Questionnaire

	@FunctionalInterface
	public interface IR4QuestionnaireProcessorFactory {
		QuestionnaireProcessor create(Repository theRepository);
	}

	@Bean
	IR4QuestionnaireProcessorFactory r4QuestionnaireProcessorFactory() {
		return r -> new QuestionnaireProcessor(r);
	}

	@Bean
	public QuestionnaireOperationsProvider r4QuestionnaireOperationsProvider() {
		return new QuestionnaireOperationsProvider();
	}

	// QuestionnaireResponse

	@FunctionalInterface
	public interface IR4QuestionnaireResponseProcessorFactory {
		QuestionnaireResponseProcessor create(Repository theRepository);
	}

	@Bean
	IR4QuestionnaireResponseProcessorFactory r4QuestionnaireResponseProcessorFactory() {
		return r -> new QuestionnaireResponseProcessor(r);
	}

	@Bean
	public QuestionnaireResponseOperationsProvider r4QuestionnaireResponseOperationsProvider() {
		return new QuestionnaireResponseOperationsProvider();
	}
}
